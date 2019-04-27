package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("messageID")
    private String messageID;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("userID")
    private String userID;

    @SerializedName("message")
    private String message;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("long")
    private double longitude;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("mType")
    private int mType;

    public Message (String messageID, String timestamp, String userID, String message,
                    String imageURL, double longitude, double latitude, int mType){

        this.messageID = messageID;
        this.timestamp = timestamp;
        this.userID = userID;
        this.message = message;
        this.imageURL = imageURL;
        this.longitude = longitude;
        this.latitude = latitude;
        this.mType = mType;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }
}
