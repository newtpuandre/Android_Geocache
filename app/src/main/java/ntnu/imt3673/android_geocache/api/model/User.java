package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userID")
    private Integer userID;

    @SerializedName("userName")
    private String userName;

    @SerializedName("passHash")
    private String passHash;

    @SerializedName("friendIDs")
    private Integer friendIDs[];

    @SerializedName("cachesFound")
    private Integer cachesFound;

    @SerializedName("distanceWalked")
    private Integer distanceWalked;

    public User(Integer userID, String userName, String passHash, Integer[] friendsIDs, Integer cachesFound, Integer distanceWalked){
        this.userID = userID;
        this.userName = userName;
        this.passHash = passHash;
        this.friendIDs = friendsIDs;
        this.cachesFound = cachesFound;
        this.distanceWalked = distanceWalked;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer[] getFriendIDs() {
        return friendIDs;
    }

    public String getPassHash() { return passHash; }

    public void setPassHash(String passHash) { this.passHash = passHash; }

    public Integer getCachesFound() { return cachesFound; }

    public void setCachesFound(Integer cachesFound) { this.cachesFound = cachesFound; }

    public Integer getDistanceWalked() { return distanceWalked; }

    public void setDistanceWalked(Integer distanceWalked) { this.distanceWalked = distanceWalked; }
}
