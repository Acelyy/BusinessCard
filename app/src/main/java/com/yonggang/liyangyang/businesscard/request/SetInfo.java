package com.yonggang.liyangyang.businesscard.request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/24.
 */

public interface SetInfo {
    @FormUrlEncoded
    @POST("editSaller")
    Observable<String> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("phone") String phone,
            @Field("discount") String discount);
}
