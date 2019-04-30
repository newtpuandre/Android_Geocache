package ntnu.imt3673.android_geocache;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MapMarkerTest {

    @Test
    public void createObjectTest(){
        MapMarker temp = new MapMarker("testID", 10.543452, 54.587958, null);

        assertEquals(temp.getMessageID(), "testID");
        assertEquals(temp.getLon(), 10.543452, 0);
        assertEquals(temp.getLat(), 54.587958, 0);

    }

    @Test
    public void setAttributes(){
        MapMarker temp = new MapMarker("", 0, 0, null);

        temp.setLat(10.1010);
        temp.setLon(20.1234);
        temp.setMessageID("MessageID123");

        assertEquals(temp.getLat(), 10.1010,0);
        assertEquals(temp.getLon(), 20.1234, 0);
        assertEquals(temp.getMessageID(), "MessageID123");
    }

}
