package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler  {
    private GoogleMap mMap;

    /**
     * Constructor
     * <p>
     *     Stores the parameter in a local variable mMap.
     * </p>
     * @param pMap
     */
    @SuppressLint("MissingPermission")
    MapHandler(GoogleMap pMap){this.mMap = pMap; mMap.setMyLocationEnabled(true);}


    /**
     * Load locations
     * <p>
     *     Loads locations and places markers on the map.
     * </p>
     */
    public void loadLocations(){
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //mMap.moveCamera();
    }

    //Debugging only.
    public void testMarker(LatLng pos){
        mMap.addMarker(new MarkerOptions().position(pos).title("test Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

}
