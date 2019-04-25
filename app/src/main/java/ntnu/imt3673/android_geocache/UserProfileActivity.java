package ntnu.imt3673.android_geocache;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ntnu.imt3673.android_geocache.data.model.LoggedInUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView username_lbl, cachesfound_num, distancewalked_num;
    private RecyclerView recyclerView;
    private ArrayList<Achievement> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle data = getIntent().getExtras();
        LoggedInUser user = data.getParcelable("user");


        this.test = AchievementHandler.returnAchievementList();
        setAchievementStatus(user);

        recyclerView = findViewById(R.id.my_recycler_view);

        AchievementViewAdapter adapter = new AchievementViewAdapter(test);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        username_lbl = findViewById(R.id.username_lbl);
        username_lbl.setText(user.getDisplayName());

        cachesfound_num = findViewById(R.id.cachefound_num);
        cachesfound_num.setText(user.getCachesFound());

        distancewalked_num = findViewById(R.id.distancewalked_num);
        distancewalked_num.setText(user.getDistanceWalked() + " Kilometers");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAchievementStatus(LoggedInUser pUser){
        //Loop over map and find all unlocked achievements.
        //Set them unlocked and show them as unlocked in the userprofile

        Map<Integer,Boolean> temp = pUser.getMyAchievements();
        for (Map.Entry<Integer,Boolean> entry : temp.entrySet()) {
            int id = entry.getKey();
            for(int i = 0; i < test.size(); i++ ){
                if (test.get(i).getId() == id) {
                    test.get(i).setUnlocked(true);
                }
            }
        }


    }

}
