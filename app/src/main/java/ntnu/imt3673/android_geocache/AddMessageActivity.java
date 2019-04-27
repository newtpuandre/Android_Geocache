package ntnu.imt3673.android_geocache;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.TestData;
import retrofit2.Call;

public class AddMessageActivity extends AppCompatActivity {
    private TextView message;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        message = findViewById(R.id.message_txt);
        sendBtn = findViewById(R.id.addMsgBtn);

        final Button addMsg = findViewById(R.id.addMsgBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        addMsg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("app1", message.getText().toString());

                //Disable input.
                sendBtn.setEnabled(false);
                message.setEnabled(false);

                //Add to database.
                new Thread(new Runnable() {
                    public void run() {
                        ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
                        Call<ArrayList<TestData>> call = taskService.getTestData();
                        ArrayList<TestData> data = null;
                        try {
                            data = call.execute().body();
                            Log.d("app1", "AddMessageData:" + data.toString());
                            addMessage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }}).start();

            }
        });
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

    private void addMessage(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message", message.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


}
