package ntnu.imt3673.android_geocache.data;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.TestData;
import ntnu.imt3673.android_geocache.data.model.LoggedInUser;
import retrofit2.Call;

/**
 * Class that handles authentication w/ login credentials and retrieves User information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(final String username, final String password) {

        final Boolean LoggedIn = true;

        /*ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
        Call<ArrayList<TestData>> call = taskService.getTestData();
        ArrayList<TestData> data = null;
        try {
            data = call.execute().body();
            Log.d("app1", "LoginData:" + data.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if (LoggedIn) {
            /*Call<ArrayList<TestData>> userInfo = taskService.getTestData();
            ArrayList<TestData> userData = null;
            try {
                userData = userInfo.execute().body();
                Log.d("app1", "userInfo" + data.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(),
                    "Test Testesen");

            //fakeUser.updateCaches();
            //fakeUser.updateDistance();

            return new Result.Success<>(fakeUser);
        } else {
            return new Result.Error(new IOException("Error logging in"));
        }

    }

    public void logout() {
        // TODO: revoke authentication
    }
}
