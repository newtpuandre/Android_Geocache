package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MUpdate {

    @SerializedName("userID")
    private String userID;

    @SerializedName("messageIDs")
    private ArrayList<String> messageIDs;


    public MUpdate(String userID, ArrayList<String> messageIDs) {
        this.userID = userID;
        this.messageIDs = messageIDs;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    public void setMessageIDs(ArrayList<String> messageIDs) {
        this.messageIDs = messageIDs;
    }

}
