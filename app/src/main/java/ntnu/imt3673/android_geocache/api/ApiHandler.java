package ntnu.imt3673.android_geocache.api;

import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.model.DistUpdate;
import ntnu.imt3673.android_geocache.api.model.MUpdate;
import ntnu.imt3673.android_geocache.api.model.Message;
import ntnu.imt3673.android_geocache.api.model.MessageRequest;
import ntnu.imt3673.android_geocache.api.model.SearchUser;
import ntnu.imt3673.android_geocache.api.model.User;
import ntnu.imt3673.android_geocache.api.model.loginRequest;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ApiHandler {


    //Root URL
    private static final String API_URL = "http://10.13.37.52:8080/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    public interface TaskService {

        @POST("/api/userinfo")
        Call<User> fetchUserInfo();

        @POST("/api/login")
        Call<User> loginUser(@Body loginRequest LoginUser);

        @POST("/api/user")
        Call<Void> registerUser(@Body User RegisterUser);

        @POST("/api/getmessages")
        Call<ArrayList<Message>> getMessages(@Body MessageRequest msgreq);

        @POST("/api/message")
        Call<String> postMessage(@Body Message postMessage);

        @POST("/api/updateMessages")
        Call<Void> updateMessages(@Body MUpdate messageUpdate);

        @POST("/api/updatedistance")
        Call<Void> updateDistance(@Body DistUpdate distanceUpdate);

        @POST("/api/getuser")
        Call<User> searchUser(@Body SearchUser sUser);

    }


}
