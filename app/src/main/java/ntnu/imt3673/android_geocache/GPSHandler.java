package ntnu.imt3673.android_geocache;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPSHandler {

    private FusedLocationProviderClient fusedLocationClient;
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
    GPSHandler(FusedLocationProviderClient pLocation) {
        fusedLocationClient = pLocation;

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
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                        }
                    }
                });

        LatLng temp = new LatLng(lat,lon);
        return temp;
    }




}
