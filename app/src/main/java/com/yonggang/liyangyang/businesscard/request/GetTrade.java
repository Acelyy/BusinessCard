package com.yonggang.liyangyang.businesscard.request;

import com.yonggang.liyangyang.businesscard.response.OrderDetailResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/27.
 */

public interface GetTrade {
    @FormUrlEncoded
    @POST("getTrade")
    Observable<OrderDetailResult> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("trade_id") String trade_id);
}
