package ntnu.imt3673.android_geocache.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntnu.imt3673.android_geocache.AchievementHandler;
import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.TestData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {

    private String userId;
    private String displayName;

    private double distanceWalked; //In Kilometers
    private int cachesFound;

    //Contains achievement ID (Integer) and True/false if it is unlocked (Bool)
    private Map<Integer,Boolean> myAchievements;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.myAchievements = new HashMap<>();

        //Load achievement data from DB
        //Dummy data right now.
        //this.myAchievements.put(0, true);
        //this.myAchievements.put(2, true);

        //Load this data from DB
        this.cachesFound = 0;
        this.distanceWalked = 0;

        this.updateCaches(34);
        this.updateDistance(52);
    }

    //Only used when searching for a user!
    public LoggedInUser(String userId, String displayName, double distanceWalked, int cachesFound) {
        this.userId = userId;
        this.displayName = displayName;
        this.myAchievements = new HashMap<>();

        this.cachesFound = 0;
        this.distanceWalked = 0;
        this.updateDistance(distanceWalked);
        this.updateCaches(cachesFound);

    }

    protected LoggedInUser(Parcel in) {
        this.userId = in.readString();
        this.displayName = in.readString();
        this.distanceWalked = in.readDouble();
        this.myAchievements = in.readHashMap(this.getClass().getClassLoader());
        this.cachesFound = in.readInt();
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCachesFound(){
        return String.valueOf(this.cachesFound);
    }

    public double getDistanceWalked(){
        return this.distanceWalked;
    }

    public Map<Integer, Boolean> getMyAchievements(){
        return this.myAchievements;
    }

    public void updateDistance(double distance){
        this.distanceWalked += distance;

        //Check if new value qualifies for new achievements
        ArrayList<Integer> ret = AchievementHandler.meetDistanceCriteria(this.distanceWalked);
        updateAchievements(ret);

        //If it does, unlock it and update db
    }

    public void updateCaches(int caches){
        this.cachesFound += caches;

        //Check if new value qualifies for new achievements
        ArrayList<Integer> ret = AchievementHandler.meetCacheCriteria(this.cachesFound);
        updateAchievements(ret);


        //If it does, unlock it and update db
    }

    public void updateAchievements(ArrayList<Integer> achivIds){
        for(int i = 0; i < achivIds.size(); i++ ){
            boolean exists = false;
            for (Map.Entry<Integer,Boolean> entry : this.myAchievements.entrySet()) {
                if(achivIds.get(i) == entry.getKey()) {
                    exists = true;
                }
            }
            if (!exists) {
                this.myAchievements.put(achivIds.get(i), true);
            }

        }
    }

    public static final Creator<LoggedInUser> CREATOR = new Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.displayName);
        dest.writeDouble(this.distanceWalked);
        dest.writeMap(this.myAchievements);
        dest.writeInt(this.cachesFound);
    }
}
