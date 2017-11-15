package com.yonggang.liyangyang.businesscard.request;

import com.yonggang.liyangyang.businesscard.response.ActivateResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public interface Activate {
    @FormUrlEncoded
    @POST("activate")
    Observable<ActivateResult> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("userid") String userid);



}
