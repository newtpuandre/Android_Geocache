package ntnu.imt3673.android_geocache;

import java.util.ArrayList;

public class AchievementHandler {


    public static ArrayList<Achievement> returnAchievementList(){
        ArrayList<Achievement> ret = new ArrayList<>();

        Achievement temp;
        //All Achievements here

        //Cache related achievements
        temp = new Achievement(0,"First Cache", "Find your first cache", "", 1, 0);
        ret.add(temp);
        temp = new Achievement(1,"Cache Hunter", "Find 100 caches", "", 100, 0);
        ret.add(temp);

        //Distance related Achievements

        temp = new Achievement(2,"Baby steps", "Walk 1 kilometer", "", 0, 1);
        ret.add(temp);
        temp = new Achievement(3,"Your feet must hurt...", "Walk 100 kilometer", "", 0, 100);
        ret.add(temp);

        return ret;
    }


}