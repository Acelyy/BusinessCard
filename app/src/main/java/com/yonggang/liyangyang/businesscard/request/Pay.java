package com.yonggang.liyangyang.businesscard.request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/23.
 */

public interface Pay {
    @FormUrlEncoded
    @POST("pay")
    Observable<String> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("card_sn") String card_sn,
            @Field("rel_money") String rel_money,
            @Field("discount") String discount,
            @Field("discount_money") String discount_money,
            @Field("casher_type") String casher_type,
            @Field("remark") String remark,
            @Field("username") String username,
            @Field("item") String item);
}
