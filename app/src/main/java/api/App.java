package api;

import android.app.Application;
import android.text.TextUtils;

import com.example.balance.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String SHARED_PREF = "pref";
    private static final String TOKEN_KEY ="auth_token";

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AuthInterceptor())
                .build();


        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        api = retrofit.create(Api.class);
    }
    public Api getApi(){
        return api;
    }

    public void saveAuthToken(String token){
        getSharedPreferences(SHARED_PREF,MODE_PRIVATE)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();//apply edit in background, not main thread
    }

    public String getAuthToken(){//достам токен
        return getSharedPreferences(SHARED_PREF,MODE_PRIVATE)
                 .getString(TOKEN_KEY,null);
    }
    public boolean isAuthorise(){
        return !TextUtils.isEmpty(getAuthToken());
    }


    private  class AuthInterceptor implements Interceptor {

        //etc., this method will intercept the request,
        //add to it auth-token and send further to the server
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            urlBuilder.addQueryParameter("auth-token",getAuthToken());
            return chain.proceed(request.newBuilder().url(urlBuilder.build()).build());

        }
    }

}
