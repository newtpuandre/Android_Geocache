package ntnu.imt3673.android_geocache.api.model;

public class TestData {
    private long userID;
    private long id;
    private String title;
    private boolean completed;

    public long getUserID() { return userID; }
    public void setUserID(long value) { this.userID = value; }

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public boolean getCompleted() { return completed; }
    public void setCompleted(boolean value) { this.completed = value; }
}