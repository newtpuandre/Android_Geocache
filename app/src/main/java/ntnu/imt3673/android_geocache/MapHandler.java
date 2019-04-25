package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ntnu.imt3673.android_geocache.data.model.LoggedInUser;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MapHandler{
    static double PI_RAD = Math.PI / 180.0;

    private GoogleMap mMap;
    private GPSHandler mGps;
    private LoggedInUser user;

    private ArrayList<Marker> markers;
    private ArrayList<LatLng> visitedMarkers;

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
     *
     * </p>
     */
    public void loadLocations(){
        //Test point 1
        Marker t;
        LatLng loc = new LatLng(60.807347,10.6780881);
        //60.8020848,10.6789751
        t = mMap.addMarker(new MarkerOptions().position(loc).title("Test point 1"));
        markers.add(t);

        //Test point 2
        //60.7929976,10.6791039
        loc = new LatLng(60.7929976,10.6791039);
        t = mMap.addMarker(new MarkerOptions().position(loc).title("Test point 2"));
        markers.add(t);

        //Test point 3
        //60.8017499,10.6771727
        loc = new LatLng(60.8017499,10.6771727);
        t = mMap.addMarker(new MarkerOptions().position(loc).title("Test point 3"));
        markers.add(t);


        updateMarkers();
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
            Marker tempMarker = markers.get(i);
            double distance = greatCircleInMeters(mypos, tempMarker.getPosition());
            Log.d("app1", "" + distance);
            if (distance >= SettingsHandler.returnSearchRadius()) {
                tempMarker.setVisible(false);
            } else {
                tempMarker.setVisible(true);
            }
        }
    }

    public boolean onMarkerClick(Marker marker, LoggedInUser user) {
        if ( visitedMarkers.size() == 0) {
            LatLng temp = marker.getPosition();
            visitedMarkers.add(temp);
            user.updateCaches(1);
            return false;
        }

        boolean found = false;

        for (int i = 0; i < visitedMarkers.size(); i++) {
            LatLng markerpos = marker.getPosition();
            LatLng loopMark = visitedMarkers.get(i);

            if (loopMark.longitude == markerpos.longitude
                    && loopMark.latitude == markerpos.latitude) {
                found = true;
            }
        }

        if (!found) {
            visitedMarkers.add(marker.getPosition());
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

    public void addLocation(String message, LatLng pos){
        Marker t;
        t = mMap.addMarker(new MarkerOptions().position(pos).title(message));
        markers.add(t);
        //Add to database.
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
