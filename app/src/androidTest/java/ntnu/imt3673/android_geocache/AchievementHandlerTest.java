package ntnu.imt3673.android_geocache;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AchievementHandlerTest {

    @Test
    public void returnAchievements(){
        ArrayList<Achievement> data = AchievementHandler.returnAchievementList();

        assertEquals(10, data.size());
    }

    @Test
    public void meetCacheCriteria(){
        ArrayList<Integer> data = AchievementHandler.meetCacheCriteria(66);
        Integer[] expectedData = {0,2,4,6};
        assertEquals(data.get(0), expectedData[0]);
        assertEquals(data.get(1), expectedData[1]);
        assertEquals(data.get(2), expectedData[2]);
        assertEquals(data.get(3), expectedData[3]);
    }

    @Test
    public void meetDistanceCriteria(){
        ArrayList<Integer> data = AchievementHandler.meetDistanceCriteria(66);
        Integer[] expectedData = {1,3,5,7};
        assertEquals(data.get(0), expectedData[0]);
        assertEquals(data.get(1), expectedData[1]);
        assertEquals(data.get(2), expectedData[2]);
        assertEquals(data.get(3), expectedData[3]);
    }


}
