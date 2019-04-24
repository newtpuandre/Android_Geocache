package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MapHandler {
    static double PI_RAD = Math.PI / 180.0;

    private GoogleMap mMap;
    private GPSHandler mGps;

    private ArrayList<Marker> markers;

    /**
     * Constructor
     * <p>
     *     Stores the parameter in a local variable mMap.
     *     Enables user blip on map
     *     Initialize ArrayList
     * </p>
     * @param pMap
     */
    @SuppressLint("MissingPermission")
    MapHandler(GoogleMap pMap, GPSHandler pGps){
        this.mMap = pMap;
        this.mGps = pGps;
        mMap.setMyLocationEnabled(true);
        markers = new ArrayList<>();


    }


    /**
     * Load locations
     * <p>
     *     Loads locations and places markers on the map.
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

        //Get current position
        LatLng mypos = mGps.getCurrentLocation();
        Log.d("app1", mypos.toString());


        for(int i = 0; i < markers.size(); i++) {
            Marker tempMarker = markers.get(i);
            double distance = greatCircleInMeters(mypos, tempMarker.getPosition());
            Log.d("app1", "" + distance);
            if (distance >= 250) {
                tempMarker.setVisible(false);
            }
        }

        /*Pseudo code
        Load markers from db into markers array.
        Loop over array and check which are supposed to be loaded (by radius)
        Show those
        Hide those who are outside of the radius

        Calculation
        https://stackoverflow.com/questions/15372705/calculating-a-radius-with-longitude-and-latitude
        */
    }

    public double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 1000;
    }

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

    public void moveToUser() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mGps.getCurrentLocation(), 14.0f));
    }

}
