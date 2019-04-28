package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userID")
    private String userID;

    @SerializedName("userName")
    private String userName;

    @SerializedName("passHash")
    private String passHash;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("cachesFound")
    private Integer cachesFound;

    @SerializedName("distanceWalked")
    private Integer distanceWalked;

    public User(String userID, String userName, String passHash, String fullName, Integer cachesFound, Integer distanceWalked){
        this.userID = userID;
        this.userName = userName;
        this.passHash = passHash;
        this.fullName = fullName;
        this.cachesFound = cachesFound;
        this.distanceWalked = distanceWalked;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getfullName() {
        return fullName;
    }

    public String getPassHash() { return passHash; }

    public void setPassHash(String passHash) { this.passHash = passHash; }

    public Integer getCachesFound() { return cachesFound; }

    public void setCachesFound(Integer cachesFound) { this.cachesFound = cachesFound; }

    public Integer getDistanceWalked() { return distanceWalked; }

    public void setDistanceWalked(Integer distanceWalked) { this.distanceWalked = distanceWalked; }
}
