package ntnu.imt3673.android_geocache.data;

import java.io.IOException;

import ntnu.imt3673.android_geocache.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves User information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(final String username, final String password) {

        final Boolean LoggedIn = true;

        /*ApiHandler.TaskService taskService = ApiHandler.createService(ApiHandler.TaskService.class);
        Thread t = new Thread();
        Call<ArrayList<TestData>> call = taskService.getTestData();
        ArrayList<TestData> data = null;
        try {
            data = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(),
                "Test Testesen");
        if (LoggedIn) {
            return new Result.Success<>(fakeUser);
        } else {
            return new Result.Error(new IOException("Error logging in"));
        }

    }

    public void logout() {
        // TODO: revoke authentication
    }
}
