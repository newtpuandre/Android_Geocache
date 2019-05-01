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
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, 26);

        assertEquals(temp.getUserId(), "testUserId");
        assertEquals(temp.getDisplayName(), "Andre testesen");
        assertEquals(temp.getDistanceWalked(), 26,0);
        assertEquals(temp.getCachesFound(), "26");
    }

    @Test
    public void updateDistanceTest(){
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, 26);
        assertEquals(temp.getDistanceWalked(), 26, 0);

        temp.updateDistance(10);
        assertEquals(temp.getDistanceWalked(), 36, 0);
    }

    @Test
    public void updateCachesTest(){
        LoggedInUser temp = new LoggedInUser("testUserId", "Andre testesen", 26, 26);
        assertEquals(temp.getCachesFound(), "26");

        temp.updateCaches(10);
        assertEquals(temp.getCachesFound(), "36");
    }

}
