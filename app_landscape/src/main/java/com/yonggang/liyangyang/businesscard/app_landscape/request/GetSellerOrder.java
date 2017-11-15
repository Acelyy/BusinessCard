package com.yonggang.liyangyang.businesscard.app_landscape.request;


import com.yonggang.liyangyang.businesscard.app_landscape.response.GetSellerOrderResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/26.
 */

public interface GetSellerOrder {
    @FormUrlEncoded
    @POST("getSallerOrder")
    Observable<GetSellerOrderResponse> doPost(
            @Field("device_no") String device_no,
            @Field("auth_code") String auth_code,
            @Field("page") int page);
}
