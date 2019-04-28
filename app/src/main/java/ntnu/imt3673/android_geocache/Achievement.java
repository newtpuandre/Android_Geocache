package ntnu.imt3673.android_geocache;

public class Achievement{

    private int id;
    private String title;
    private String summary;
    private int cacheCriteria; //Amount of caches needed to be found for achievement.
    private int distanceCriteria; //Amount of distance needed to be walked for achievement.
    private boolean unlocked = false;

    public Achievement(int pId,String pTitle, String pSummary, int pCacheCriteria, int pDistanceCriteria){
        this.id = pId;
        this.title = pTitle;
        this.summary = pSummary;
        this.cacheCriteria = pCacheCriteria;
        this.distanceCriteria = pDistanceCriteria;
    }

    public int getId(){return this.id;}

    public String getTitle() {
        return this.title;
    }

    public String getSummary() {
        return this.summary;
    }

    public int getCacheCriteria() {
        return cacheCriteria;
    }

    public int getDistanceCriteria() {
        return distanceCriteria;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}