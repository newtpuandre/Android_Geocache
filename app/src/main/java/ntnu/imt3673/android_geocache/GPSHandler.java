package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class GPSHandler {

    private LocationManager locManager;

    /**
     * Constructor
     * <p>
     *     Takes LocationManager as a parameter and starts fetching
     *     coordinates.
     * </p>
     *
     */
    @SuppressLint("MissingPermission") //This is fixed with a check in MapsActivity.java.
    GPSHandler(LocationManager pLocManager) {
        this.locManager = pLocManager;
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
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locManager.getLastKnownLocation(locationProvider);
        LatLng temp = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        return temp;
    }




}
