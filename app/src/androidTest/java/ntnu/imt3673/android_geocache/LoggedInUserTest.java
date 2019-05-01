package ntnu.imt3673.android_geocache;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ntnu.imt3673.android_geocache.data.model.LoggedInUser;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoggedInUserTest {

    @Test
    public void createObjectTest(){
        ArrayList<String> stringArr = new ArrayList<>();
        stringArr.add("test");
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, stringArr);

        assertEquals(temp.getUserId(), "testUserId");
        assertEquals(temp.getDisplayName(), "Andre testesen");
        assertEquals(temp.getDistanceWalked(), 26,0);
        assertEquals(temp.getCachesFound().size(), 1);
    }

    @Test
    public void updateDistanceTest(){
        ArrayList<String> stringArr = new ArrayList<>();
        stringArr.add("test");
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, stringArr);
        assertEquals(temp.getDistanceWalked(), 26, 0);

        temp.updateDistance(10);
        assertEquals(temp.getDistanceWalked(), 36, 0);
    }

    @Test
    public void updateCachesTest(){
        ArrayList<String> stringArr = new ArrayList<>();
        stringArr.add("test");
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, stringArr);
        assertEquals(temp.getCachesFound().size(), 1);

        stringArr.add("test");
        temp.setCaches(stringArr);
        assertEquals(temp.getCachesFound().size(), 3);
    }

}
