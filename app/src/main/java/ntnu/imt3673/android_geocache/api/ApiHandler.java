package ntnu.imt3673.android_geocache.api;

import java.util.ArrayList;

import ntnu.imt3673.android_geocache.api.model.Message;
import ntnu.imt3673.android_geocache.api.model.TestData;
import ntnu.imt3673.android_geocache.api.model.User;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ApiHandler {


    //Root URL
    private static final String API_URL = "https://jsonplaceholder.typicode.com";

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

        //Only for debug
        @GET("/todos")
        Call<ArrayList<TestData>> getTestData();

        @GET("/User")
        Call<User> fetchUserInfo();

        @POST("/login")
        Call<User> loginUser(@Body User LoginUser, Callback<User> cb);

        @POST("/User")
        Call<User> registerUser(@Body User RegisterUser, Callback<User> cb);

        @GET("/getMessages")
        Call<ArrayList<Message>> getMessages();

        @POST("/postMessage")
        Call<Message> postMessage(@Body Message postMessage, Callback<Message> cb);

    }


}