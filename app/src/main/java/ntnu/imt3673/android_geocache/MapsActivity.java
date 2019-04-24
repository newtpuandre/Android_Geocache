package ntnu.imt3673.android_geocache;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import ntnu.imt3673.android_geocache.data.LoginDataSource;
import ntnu.imt3673.android_geocache.data.LoginRepository;



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    static final int ADD_MESSAGE_REQUEST = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;


    private DrawerLayout drawerLayout;
    private MapHandler gMapsHandler;

    private SettingsHandler mSettingsHandler;

    public GoogleMap mMap;
    public GPSHandler mGPS;


    private LoginRepository loginRepo;
    private LoginDataSource loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Login setup
        loginData = new LoginDataSource();
        loginRepo = LoginRepository.getInstance(loginData);

        //Check for GPS permissions
        checkForPermissions();
        mGPS = new GPSHandler((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));

        //Prime the gps. (First always return 0,0)
        mGPS.getCurrentLocation();

        mSettingsHandler = new SettingsHandler(this.getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        //TODO: Change from toString to something less hardcoded.
                        switch(menuItem.toString()){
                            case "Settings":
                                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                                startActivity(intent);

                                break;
                            case "Add a message":
                                Intent addMsgIntent = new Intent(MapsActivity.this, AddMessageActivity.class);
                                startActivityForResult(addMsgIntent,ADD_MESSAGE_REQUEST);
                                break;

                            case "My profile":
                                Intent myProfile = new Intent(MapsActivity.this, UserProfileActivity.class);
                                startActivity(myProfile);
                                break;
                        }
                        Log.d("Android_Geocache", "" + menuItem.getItemId());
                        return true;
                    }
                });

        //Check if user is logged in.
       /* if(!loginRepo.isLoggedIn()){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_MESSAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    String ret = data.getStringExtra("message");
                    gMapsHandler.addLocation(ret, mGPS.getCurrentLocation());

                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gMapsHandler = new MapHandler(googleMap, mGPS);
        gMapsHandler.loadLocations();
        gMapsHandler.moveToUser();

    }


    /**
     * Checks For Permissions
     * <p>
     * Prompts the user for location permission if it isn't already given.
     * </p>
     *
     */
    public void checkForPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //TODO: Handle user declining the permission prompt.
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



}
