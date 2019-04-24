package ntnu.imt3673.android_geocache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsHandler {
    private SharedPreferences sharedPref;

    private static int SEARCH_RADIUS = 250;

    public SettingsHandler(Context context){
        //Setup preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //Grab search radius and save it
        SEARCH_RADIUS = sharedPref.getInt("map_search_radius", 250);
    }

    /**
     * Return search radius
     * <p>
     *     Returns currently set search radius
     * </p>
     * @return
     */
    public static int returnSearchRadius(){
        return SEARCH_RADIUS;
    }

    /**
     * Set search radius
     * <p>
     *     Sets the current search radius
     * </p>
     * @param pRadius
     */
    public static void setSearchRadius(int pRadius) {
        SEARCH_RADIUS = pRadius;
    }


}
