package ntnu.imt3673.android_geocache;

import com.google.android.gms.maps.model.Marker;

public class MapMarker {

    private String MessageID;

    private double lon;

    private double lat;

    private Marker myMark;

    public MapMarker(String messageID, double lon, double lat, Marker myMark) {
        MessageID = messageID;
        this.lon = lon;
        this.lat = lat;
        this.myMark = myMark;
    }

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String messageID) {
        MessageID = messageID;
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

    public Marker getMyMark() {
        return myMark;
    }

    public void setMyMark(Marker myMark) {
        this.myMark = myMark;
    }
}
