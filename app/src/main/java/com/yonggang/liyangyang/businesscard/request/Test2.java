package com.yonggang.liyangyang.businesscard.request;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public interface Test2 {
    @POST("testpro/servlet/login")
    Observable<String> test(@Query("name_") String name);
}
