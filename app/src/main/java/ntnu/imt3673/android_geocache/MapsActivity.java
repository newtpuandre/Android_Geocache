package ntnu.imt3673.android_geocache;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.google.android.gms.maps.model.Marker;

import ntnu.imt3673.android_geocache.data.LoginDataSource;
import ntnu.imt3673.android_geocache.data.LoginRepository;
import ntnu.imt3673.android_geocache.ui.login.LoginActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    static final int ADD_MESSAGE_REQUEST = 0;
    static final int SETTINGS_MENU = 1;
    static final int RESULT_LOGOUT = 10;

    private DrawerLayout drawerLayout;
    private MapHandler gMapsHandler;

    private SettingsHandler mSettingsHandler;

    public GoogleMap mMap;
    public GPSHandler mGPS;

    public static LoginRepository loginRepo;
    private LoginDataSource loginData;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSettingsHandler = new SettingsHandler(this.getApplicationContext());

        handler = new Handler();

        //Login setup
        loginData = new LoginDataSource();
        loginRepo = LoginRepository.getInstance(loginData);

        // Setup the nav view
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mGPS = new GPSHandler((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_MESSAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    String ret = data.getStringExtra("message");
                    String messageID = data.getStringExtra("messageID");

                    //Visually add marker to map.
                    gMapsHandler.addLocation(messageID, ret, mGPS.getCurrentLocation());
                }
                break;
            case SETTINGS_MENU:
                switch(resultCode){
                    case RESULT_OK:
                        gMapsHandler.updateMarkers();
                        break;
                    case RESULT_LOGOUT:
                        loginRepo.logout();
                        Intent loginIntent = new Intent(this, LoginActivity.class);
                        //Destroy maps activity and start login activity
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        break;
                }
                break;
        }
    }

    /**
     * Called then the physical back button is clicked
     */
    @Override
    public void onBackPressed() {
        // Close the navigation view if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when the navigation view button is clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    /**
     * Called when a menu item in the navigation view is clicked
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle individual item clicks
        switch(menuItem.getItemId()){
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, SETTINGS_MENU);

                break;
            case R.id.nav_add_message:
                Intent addMessageIntent = new Intent(MapsActivity.this, AddMessageActivity.class);
                startActivityForResult(addMessageIntent,ADD_MESSAGE_REQUEST);
                break;

            case R.id.nav_profile:
                Intent profileIntent = new Intent(MapsActivity.this, UserProfileActivity.class);
                profileIntent.putExtra("user", loginRepo.returnUser());
                startActivity(profileIntent);
                break;

            case R.id.nav_user_search:
                Intent usersearchIntent = new Intent(MapsActivity.this, SearchUserActivity.class);
                startActivity(usersearchIntent);
        }

        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        gMapsHandler = new MapHandler(googleMap, mGPS, this, loginRepo.returnUser(), this);

        new Thread(new Runnable() {
            public void run() {
                gMapsHandler.loadLocations(MapsActivity.this);
            }}).start();

        gMapsHandler.moveToUser();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                gMapsHandler.onMarkerClick(marker, loginRepo.returnUser());
                return false;
            }
        });

        handler.postDelayed(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                gMapsHandler.loadLocations(MapsActivity.this); //Get new markers from db.
                    }}).start();
                handler.postDelayed(this, 120000); //now is every 2 minutes
            }
        }, 120000); //Every 120000 ms (2 minutes)

    }


    /**
     * Checks For Permissions
     * <p>
     * Prompts the user for location permission if it isn't already given.
     * </p>
     *
     */

}
