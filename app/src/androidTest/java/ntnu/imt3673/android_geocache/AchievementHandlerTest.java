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
        /*ArrayList<Integer> expectedData = {};
        assertArrayEquals(data, );*/

    }

    @Test
    public void meetDistanceCriteria(){
        ArrayList<Integer> data = AchievementHandler.meetDistanceCriteria(66);
        /*
        ArrayList<Integer> expectedData = {};
        assertArrayEquals(data, );
         */
    }


}
