package ntnu.imt3673.android_geocache.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntnu.imt3673.android_geocache.AchievementHandler;
import ntnu.imt3673.android_geocache.MapsActivity;
import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.DistUpdate;
import ntnu.imt3673.android_geocache.api.model.MUpdate;
import ntnu.imt3673.android_geocache.api.model.User;
import ntnu.imt3673.android_geocache.api.model.loginRequest;
import retrofit2.Call;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {

    private String userId;
    private String displayName;

    private double distanceWalked; //In Kilometers
    private ArrayList<String> cachesFound;

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
        this.cachesFound = new ArrayList<>();
        this.distanceWalked = 0;

        /*this.updateCaches(34);
        this.updateDistance(52);*/
    }

    //Only used when searching for a user!
    public LoggedInUser(String userId, String displayName, double distanceWalked, ArrayList<String> cachesFound) {
        this.userId = userId;
        this.displayName = displayName;
        this.myAchievements = new HashMap<>();

        this.cachesFound = new ArrayList<>();
        this.distanceWalked = 0;
        this.setDistance(distanceWalked);
        this.setCaches(cachesFound);

    }

    protected LoggedInUser(Parcel in) {
        this.userId = in.readString();
        this.displayName = in.readString();
        this.distanceWalked = in.readDouble();
        this.myAchievements = in.readHashMap(this.getClass().getClassLoader());
        this.cachesFound = in.readArrayList(this.getClass().getClassLoader());
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList<String> getCachesFound(){
        return this.cachesFound;
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
        new Thread(new Runnable() {
            public void run() {
                ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
                DistUpdate temp = new DistUpdate(userId, distanceWalked);
                Call<Void> call = taskService.updateDistance(temp);
                try {
                    call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();
    }

    public void setDistance(double distance){
        this.distanceWalked += distance;

        //Check if new value qualifies for new achievements
        ArrayList<Integer> ret = AchievementHandler.meetDistanceCriteria(this.distanceWalked);
        updateAchievements(ret);
    }

    public void updateCaches(ArrayList<String> caches){
        this.cachesFound = caches;


        //Check if new value qualifies for new achievements
        ArrayList<Integer> ret = AchievementHandler.meetCacheCriteria(this.cachesFound.size());
        updateAchievements(ret);

        new Thread(new Runnable() {
            public void run() {
                ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
                MUpdate temp = new MUpdate(userId, cachesFound);
                Call<Void> call = taskService.updateMessages(temp);
                try {
                   call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();
    }

    public void setCaches(ArrayList<String> caches){
        for(int i = 0; i < caches.size(); i++) {
            this.cachesFound.add(caches.get(i));
        }

        //Check if new value qualifies for new achievements
        ArrayList<Integer> ret = AchievementHandler.meetCacheCriteria(this.cachesFound.size());
        updateAchievements(ret);
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
        dest.writeList(this.cachesFound);
    }
}
