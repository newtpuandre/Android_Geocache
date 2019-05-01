package ntnu.imt3673.android_geocache.api.model;

import com.google.gson.annotations.SerializedName;

public class loginRequest {

    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    public loginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
