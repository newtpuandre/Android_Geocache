package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapHandler {
    private GoogleMap mMap;

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
    MapHandler(GoogleMap pMap){
        this.mMap = pMap;
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
        /*Pseudo code
        Load markers from db into markers array.
        Loop over array and check which are supposed to be loaded (by radius)
        Show those
        Hide those who are outside of the radius

        Calculation
        https://stackoverflow.com/questions/15372705/calculating-a-radius-with-longitude-and-latitude
        */
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

    public void moveToUser(LatLng pos) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14.0f));
    }

}
