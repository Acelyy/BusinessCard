package com.yonggang.liyangyang.businesscard.request;

import com.yonggang.liyangyang.businesscard.response.GetSellerOrderResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/4/7.
 */

public interface GetSellerList {
    @FormUrlEncoded
    @POST("getSallerlist")
    Observable<GetSellerOrderResponse> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date);

}
