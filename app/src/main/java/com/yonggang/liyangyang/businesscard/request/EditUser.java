package com.yonggang.liyangyang.businesscard.request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/27.
 */

public interface EditUser {
    @FormUrlEncoded
    @POST("editUser")
    Observable<String> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("oldpassword") String oldpassword,
            @Field("password") String password,
            @Field("userid") String userid);
}
