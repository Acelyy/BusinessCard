package com.yonggang.liyangyang.businesscard.app_landscape.request;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.User;
import com.yonggang.liyangyang.businesscard.app_landscape.response.LoginResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public interface LoginSever {
    @FormUrlEncoded//读参数进行urlEncoded
    @POST("login")
    Observable<LoginResult> doPost(
            @Field("username") String user_name,
            @Field("password") String password,
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code);

    @GET("Outsides/login")
    Observable<User> doGet(
            @Query("username") String user_name,
            @Query("password") String password,
            @Query("device_no") String device_no,
            @Query("auth_code") String auth_code);
}
