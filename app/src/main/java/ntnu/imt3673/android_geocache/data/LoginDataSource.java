package ntnu.imt3673.android_geocache.data;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.ApiHandler;
import ntnu.imt3673.android_geocache.api.model.User;
import ntnu.imt3673.android_geocache.api.model.loginRequest;
import ntnu.imt3673.android_geocache.data.model.LoggedInUser;
import ntnu.imt3673.android_geocache.ui.login.LoginActivity;
import retrofit2.Call;

/**
 * Class that handles authentication w/ login credentials and retrieves User information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(final String username, final String password) {

        Boolean LoggedIn = false;

        ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
        loginRequest tempuser = new loginRequest(username, password);
        Call<User> call = taskService.loginUser(tempuser);
        User retUser = null;
        try {
            retUser = call.execute().body();
            if(retUser != null) {
                LoggedIn = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (LoggedIn) {
            Log.d("app1", "LoginData:" + retUser.toString());
            LoggedInUser newUser = new LoggedInUser(retUser.getUserID(),
                    retUser.getfullName(), retUser.getDistanceWalked(), retUser.getCachesFound());
            return new Result.Success<>(newUser);
        } else {
            return new Result.Error(new IOException("Error logging in"));
        }
    }

    public Result<LoggedInUser> register(String name, String email, String password, String confirmedpassword) {
        try {
            ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
            ArrayList<String> empty = new ArrayList<>();
            User tempuser = new User("", email, password, name, empty, 0);
            Call<Void> call = taskService.registerUser(tempuser);
            try {
                call.execute();
                Log.d("app1", "LoginData:" + tempuser.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return login(email, password);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
