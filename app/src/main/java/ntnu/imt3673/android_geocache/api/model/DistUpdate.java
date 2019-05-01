package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class DistUpdate {

    @SerializedName("userID")
    private String userID;

    @SerializedName("distanceWalked")
    private double distanceWalked;


    public DistUpdate(String userID, double distanceWalked) {
        this.userID = userID;
        this.distanceWalked = distanceWalked;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void setDistanceWalked(double distanceWalked) {
        this.distanceWalked = distanceWalked;
    }
}
