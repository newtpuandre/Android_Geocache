package ntnu.imt3673.android_geocache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.Message;
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

                if (message.getText().length() > 0) {
                    //Disable input.
                    sendBtn.setEnabled(false);
                    message.setEnabled(false);

                    //Add to database.
                    new Thread(new Runnable() {
                        public void run() {
                            ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
                            LatLng temp = GPSHandler.getCurrentLocation();
                            Message tempMsg = new Message("","",MapsActivity.loginRepo.returnUser().getUserId(), message.getText().toString(),
                                "test", temp.longitude, temp.latitude, 2);
                            Call<String> call = taskService.postMessage(tempMsg);
                            try {
                                String ret = call.execute().body();
                                Log.d("app1", "RESPONSE: "+ ret);
                                if (ret.equals("")) {
                                    Log.d("app1", "Error posting message");
                                    Toast.makeText(AddMessageActivity.this.getApplicationContext(), "Could not post message", Toast.LENGTH_LONG);
                                } else {
                                    addMessage(ret);
                                }
                            } catch (IOException e) {
                               e.printStackTrace();
                            }
                        }}).start();

                }
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

    private void addMessage(String messageID){
        Log.d("app1", "addMsg MESSAGEID: " + messageID);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message", message.getText().toString());
        returnIntent.putExtra("messageID", messageID);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


}
