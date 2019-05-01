package ntnu.imt3673.android_geocache;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.Message;
import ntnu.imt3673.android_geocache.api.model.MessageRequest;
import ntnu.imt3673.android_geocache.api.model.SearchUser;
import ntnu.imt3673.android_geocache.api.model.User;
import ntnu.imt3673.android_geocache.data.model.LoggedInUser;
import retrofit2.Call;

public class SearchUserActivity extends AppCompatActivity implements SearchViewAdapter.ItemClickListener{

    private EditText searchPrompt;
    private Button searchBtn;
    private RecyclerView recyclerView;

    private ArrayList<User> searchResults;

    private SearchViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchBtn = findViewById(R.id.search_btn);
        searchPrompt = findViewById(R.id.searchbox_txt);

        recyclerView = findViewById(R.id.userRecyclerView);

        searchResults = new ArrayList<>();

        adapter = new SearchViewAdapter(searchResults);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        adapter.setClickListener(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchPrompt.getText().toString().equals("")){
                    Log.d("app1", "SearchPromt is empty");
                    return;
                }
                searchResults.clear();
                new Thread(new Runnable() {
                    public void run() {
                        SearchUser(searchPrompt.getText().toString());
                    }}).start();
            }
        });

    }

    private void SearchUser(String searchQuery){

        ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
        SearchUser tempReq = new SearchUser(searchQuery);
        Call<User> call = taskService.searchUser(tempReq);
        User temp = null;
        try {
            temp = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( temp != null) {
            searchResults.add(temp);
        }

        runOnUiThread(new Runnable(){
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
                /*User tempuser = new User("temp", "andgun96@gmail.com", "", "Andre Testesen", 25, 55);
                searchResults.add(tempuser);

                tempuser = new User("temp", "Test@test.test", "", "Tester Testesen", 1785, 650);
                searchResults.add(tempuser);*/
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

    @Override
    public void onItemClick(View view, int position) { //Position corresponds to the item number in the recyclerView
        Intent profileIntent = new Intent(SearchUserActivity.this, UserProfileActivity.class);

        //Setup selected user
        LoggedInUser temp = new LoggedInUser("", searchResults.get(position).getfullName(),
                searchResults.get(position).getDistanceWalked(), searchResults.get(position).getCachesFound());
        profileIntent.putExtra("user", temp);

        startActivity(profileIntent);
    }
}
