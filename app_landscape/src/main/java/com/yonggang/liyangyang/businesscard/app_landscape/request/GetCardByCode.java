package com.yonggang.liyangyang.businesscard.app_landscape.request;


import com.yonggang.liyangyang.businesscard.app_landscape.response.GetCardByCodeResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public interface GetCardByCode {
    @FormUrlEncoded
    @POST("getCardByCode")
    Observable<GetCardByCodeResult> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("code") String code);

}
