package com.yonggang.liyangyang.businesscard.app_landscape.request;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public interface Test {
    @GET("equipmentrepair/jbxx/yhgl/checkUser_app.action")
    Observable<String> test(@Query("yhgl.ownercode") String name, @Query("yhgl.password") String pass);
}
