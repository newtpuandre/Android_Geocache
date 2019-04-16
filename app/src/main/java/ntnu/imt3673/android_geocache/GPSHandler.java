package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class GPSHandler {

    private  LocationListener locListen;
    private double lat; //Latitude
    private double lon; //Longitude

    /**
     * Constructor
     * <p>
     *     Takes LocationManager as a parameter and starts fetching
     *     coordinates.
     * </p>
     * @param pLocation
     */
    @SuppressLint("MissingPermission") //This is fixed with a check in MapsActivity.java.
    GPSHandler(LocationManager pLocation) {

        // Define a listener that responds to location updates
       locListen = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                lat = location.getLatitude();
                lon = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        pLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListen);
    }

    /**
     * Gets the current location
     * <p>
     *     Returns the last updates Latitude and Longitude of the phone.
     * </p>
     * @return LatLng
     */
    @SuppressLint("MissingPermission")
    public LatLng getCurrentLocation(){
        LatLng temp = new LatLng(lat,lon);
        return temp;
    }




}
