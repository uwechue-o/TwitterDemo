package com.dmt.twitterdemo;

import com.dmt.twitterdemo.model.UserModel_parentNode;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ouwechue on 3/16/17.
 */

public interface ITwitterAPI {
    String BASE_URL = "https://api.twitter.com";

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<OAuthToken> postCredentials(@Field("grant_type") String grantType);

    //  https://api.twitter.com/1.1/search/tweets.json?q=%23<hashtag string>&result_type=popular&count=100
    @GET("/1.1/search/tweets.json")
    Observable<UserModel_parentNode> getUserInfo(@Query("q") String q,
                                           @Query("result_type") String r,
                                           @Query("count") String cnt);
    //Call<UserModel_parentNode> getUserInfo(@Query("q") String q,
    //                                       @Query("result_type") String r,
    //                                       @Query("count") String cnt);

    //fetch an image
    @GET
    Call<ResponseBody> fetchImage(@Url String url);
}
