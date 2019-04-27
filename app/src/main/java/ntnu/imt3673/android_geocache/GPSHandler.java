package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GPSHandler {

    private static LocationManager locManager;

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
    public static LatLng getCurrentLocation(){
        String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
        String locationProviderGPS = LocationManager.GPS_PROVIDER;

        Location lastKnownLocationNetwork = locManager.getLastKnownLocation(locationProviderNetwork);
        Location lastKnownLocationGPS = locManager.getLastKnownLocation(locationProviderGPS);

        LatLng ret;

        if (lastKnownLocationNetwork == null) { //Network Unavailable, Use GPS
            ret = new LatLng(lastKnownLocationGPS.getLatitude(), lastKnownLocationGPS.getLongitude());
        } else { //Network Available
            ret = new LatLng(lastKnownLocationNetwork.getLatitude(), lastKnownLocationNetwork.getLongitude());
        }

        return ret;
    }




}
