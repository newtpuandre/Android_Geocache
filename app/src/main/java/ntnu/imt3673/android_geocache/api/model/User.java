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

    public User(Integer userID, String userName, String passHash, Integer[] friendsIDs){
        this.userID = userID;
        this.userName = userName;
        this.passHash = passHash;
        this.friendIDs = friendsIDs;
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

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }
}
