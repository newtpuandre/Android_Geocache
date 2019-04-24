package ntnu.imt3673.android_geocache;

import android.content.SharedPreferences;

public class SettingsHandler {
    private SharedPreferences sharedPref;

    private static int SEARCH_RADIUS = 250;

    public SettingsHandler(){

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
