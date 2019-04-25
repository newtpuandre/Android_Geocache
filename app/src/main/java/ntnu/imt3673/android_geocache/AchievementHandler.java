package ntnu.imt3673.android_geocache;

import android.util.Log;

import java.util.ArrayList;

public class AchievementHandler {

    public static ArrayList<Achievement> returnAchievementList(){
        ArrayList<Achievement> ret = new ArrayList<>();
        Achievement temp;
        //All Achievements here

        //Cache related achievements
        temp = new Achievement(0,"First Cache", "Find your first cache", "", 1, -1);
        ret.add(temp);
        temp = new Achievement(2,"10 Caches", "Find 10 caches", "", 10, -1);
        ret.add(temp);
        temp = new Achievement(4,"25 Caches", "Find 25 caches", "", 25, -1);
        ret.add(temp);
        temp = new Achievement(6,"Cache Hunter", "Find 50 caches", "", 50, -1);
        ret.add(temp);
        temp = new Achievement(8,"Cache Master", "Find 100 caches", "", 100, -1);
        ret.add(temp);

        //Distance related Achievements

        temp = new Achievement(1,"Baby steps", "Walk 1 kilometer", "", -1, 1);
        ret.add(temp);
        temp = new Achievement(3,"Seasoned Walker", "Walk 10 kilometer", "", -1, 10);
        ret.add(temp);
        temp = new Achievement(5,"You are doing great", "Walk 25 kilometer", "", -1, 25);
        ret.add(temp);
        temp = new Achievement(7,"Getting sore?", "Walk 50 kilometer", "", -1, 50);
        ret.add(temp);
        temp = new Achievement(9,"Your feet must hurt...", "Walk 100 kilometer", "", -1, 100);
        ret.add(temp);

        return ret;
    }

    public static ArrayList<Integer> meetCacheCriteria(int cacheNum){
        ArrayList<Achievement> ret = returnAchievementList();
        ArrayList<Integer> achievementsQualified = new ArrayList<>();


        for(int i = 0; i < ret.size(); i++) {
            if (ret.get(i).getCacheCriteria() != -1 && ret.get(i).getCacheCriteria() <= cacheNum){
                achievementsQualified.add(ret.get(i).getId());
            }
        }

        return achievementsQualified;
    }

    public static ArrayList<Integer> meetDistanceCriteria(int distanceNum){
        ArrayList<Achievement> ret = returnAchievementList();
        ArrayList<Integer> achievementsQualified = new ArrayList<>();

        for(int i = 0; i < ret.size(); i++) {
            if (ret.get(i).getDistanceCriteria() != -1 && ret.get(i).getDistanceCriteria() <= distanceNum){
                achievementsQualified.add(ret.get(i).getId());
            }
        }

        return achievementsQualified;
    }


}