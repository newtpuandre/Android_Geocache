package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class SearchUser {
    @SerializedName("userName")
    private String userName;

    public SearchUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
