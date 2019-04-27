package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class MessageRequest {
    @SerializedName("userID")
    private String userID;

    @SerializedName("long")
    private double lon;

    @SerializedName("lat")
    private double lat;


    public MessageRequest(String userID, double lon, double lat) {
        this.userID = userID;
        this.lon = lon;
        this.lat = lat;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}