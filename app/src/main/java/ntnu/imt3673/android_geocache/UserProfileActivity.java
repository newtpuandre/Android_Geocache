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

import ntnu.imt3673.android_geocache.data.model.LoggedInUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView username_lbl;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Debug data
        ArrayList<String> test = new ArrayList<>();
        test.add("Test 1");
        test.add("Test 2");
        test.add("Test 3");
        test.add("Test 4");
        test.add("Test 5");
        test.add("Test 6");
        test.add("Test 7");

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

        Bundle data = getIntent().getExtras();
        LoggedInUser user = data.getParcelable("user");

        username_lbl = findViewById(R.id.username_lbl);
        username_lbl.setText(user.getDisplayName());
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
}
