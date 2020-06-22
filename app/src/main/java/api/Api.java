package api;

import com.example.balance.Record;

import java.util.List;

import models.AddItemResult;
import models.AuthResult;
import models.BalanceResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

   @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id")String userId);

    @GET("items")
   Call<List<Record>> getRecord(@Query("type")String type);
    //every time use the app get the token out the sharedpreference and
    //put it tn here

    @POST("items/add")
    Call<AddItemResult> addRecord(@Query("price") String prise,@Query("name") String name, @Query("type") String type);

     @GET("balance")
    Call<BalanceResult> balance();

}
