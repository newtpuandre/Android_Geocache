package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.Message;
import ntnu.imt3673.android_geocache.api.model.MessageRequest;
import ntnu.imt3673.android_geocache.data.model.LoggedInUser;
import retrofit2.Call;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MapHandler{
    static double PI_RAD = Math.PI / 180.0;

    private GoogleMap mMap;
    private GPSHandler mGps;
    private LoggedInUser user;

    private ArrayList<MapMarker> markers;
    private ArrayList<MapMarker> visitedMarkers;

    /**
     * Constructor
     * <p>
     *     Stores the parameter pMap in a local variable mMap.
     *     Store the parameter pGps in a local variable mGps.
     *     Stores the parameter pUser in a local variable user.
     *     Enables user blip on map.
     *     Initialize ArrayList.
     * </p>
     * @param pMap
     * @param pGps
     */
    @SuppressLint("MissingPermission")
    MapHandler(GoogleMap pMap, GPSHandler pGps){
        this.mMap = pMap;
        this.mGps = pGps;
        mMap.setMyLocationEnabled(true);
        markers = new ArrayList<>();

        //Visited markers should be loaded from DB with users visited caches
        //Might have to be loaded in the function call onMarkerClick if arraylist is empty
        //to prevent crash
        visitedMarkers = new ArrayList<>();

    }


    /**
     * Load locations
     * <p>
     *     Loads locations and places markers on the map.
     *     This function MUST be run in a seperate thread!
     * </p>
     * @param act
     */
    public void loadLocations(Activity act){

        //Load data from DB
        ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
        LatLng temp = mGps.getCurrentLocation();
        MessageRequest tempReq = new MessageRequest("temp", temp.longitude, temp.latitude);
        Call<ArrayList<Message>> call = taskService.getMessages(tempReq);
        ArrayList<Message> data = null;
        try {
            data = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Anything related to updating the map MUST be run in Main thread
        final ArrayList<Message> finalData = data;
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng loc;
                Marker t;
                MapMarker temp;
                if (finalData.size() != 0) {
                    for (int i = 0; i < finalData.size(); i++) {
                        loc = new LatLng(finalData.get(i).getLatitude(), finalData.get(i).getLongitude());
                        t = mMap.addMarker(new MarkerOptions().position(loc).title(finalData.get(i).getMessage()));

                        temp = new MapMarker(finalData.get(i).getMessageID(), finalData.get(i).getLongitude(),
                                finalData.get(i).getLatitude(), t);

                        //Keep a refrence of the object for later.
                        t.setTag(temp);
                        markers.add(temp);
                    }
                    updateMarkers();
                }
            }
        });
        //updateMarkers();
        /*Pseudo code
        Load markers from db into markers array.
        Loop over array and check which are supposed to be loaded (by radius) Use function updateMarkers
        */
    }


    /**
     * Update markers
     * <p>
     *     Update markers based on a new location provided by the GPS.
     * </p>
     */
    public void updateMarkers(){
        //Get current position
        LatLng mypos = mGps.getCurrentLocation();
        Log.d("app1", mypos.toString());

        for(int i = 0; i < markers.size(); i++) {
            MapMarker tempMarker = markers.get(i);
            LatLng markerPos = new LatLng(tempMarker.getLat(),tempMarker.getLon());
            double distance = greatCircleInMeters(mypos, markerPos);
            Log.d("app1", "" + distance);
            if (distance >= SettingsHandler.returnSearchRadius()) {
                tempMarker.getMyMark().setVisible(false);
            } else {
                tempMarker.getMyMark().setVisible(true);
            }
        }
    }

    public boolean onMarkerClick(Marker marker, LoggedInUser user) {
        if ( visitedMarkers.size() == 0) {
            LatLng temp = marker.getPosition();
            MapMarker clickMark = (MapMarker) marker.getTag();
            visitedMarkers.add(clickMark);
            user.updateCaches(1);
            return false;
        }

        boolean found = false;

        MapMarker clickMark = (MapMarker) marker.getTag();
        for (int i = 0; i < visitedMarkers.size(); i++) {
            MapMarker loopMark = visitedMarkers.get(i);

            if (loopMark.getMessageID() == clickMark.getMessageID()) {
                found = true;
            }
        }

        if (!found) {
            visitedMarkers.add(clickMark);
            user.updateCaches(1);
        }


        return false;
    }

    /**
     * Calculate distance between points in meters
     * @param latLng1
     * @param latLng2
     * @return
     */
    public double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 1000;
    }

    /**
     * Calculate distance between points in kilometers
     * @param lat1
     * @param long1
     * @param lat2
     * @param long2
     * @return
     */
    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }

    /**
     * Add location
     * <p>
     *     Adds location to the map and to the database.
     * </p>
     * @param message
     * @param pos
     */

    public void addLocation(String messageID,String message, LatLng pos){
        Marker t;
        t = mMap.addMarker(new MarkerOptions().position(pos).title(message));
        MapMarker temp = new MapMarker(messageID, pos.longitude, pos.latitude,t);
        markers.add(temp);
    }

    /**
     * Move camera to users
     * <p>
     *     Moves the camera to the users coordinates and zoom in.
     * </p>
     */
    public void moveToUser() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mGps.getCurrentLocation(), 14.0f));
    }

}
