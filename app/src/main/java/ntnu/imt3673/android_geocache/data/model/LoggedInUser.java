package ntnu.imt3673.android_geocache.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {

    private String userId;
    private String displayName;

    private int distanceWalked; //In Kilometers
    private int cachesFound;

    private Map<String,Boolean> myAchievements;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.myAchievements = new HashMap<>();
        //this.cachesFound =
    }

    protected LoggedInUser(Parcel in) {
        this.userId = in.readString();
        this.displayName = in.readString();
        this.distanceWalked = in.readInt();
        this.myAchievements = in.readHashMap(this.getClass().getClassLoader());
        this.cachesFound = in.readInt();
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static final Creator<LoggedInUser> CREATOR = new Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.displayName);
        dest.writeInt(this.distanceWalked);
        dest.writeMap(this.myAchievements);
        dest.writeInt(this.cachesFound);
    }
}
