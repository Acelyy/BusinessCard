package com.yonggang.liyangyang.testretrofix.httpUtil;

import com.yonggang.liyangyang.testretrofix.response.HttpResult;
import com.yonggang.liyangyang.testretrofix.response.Title;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liyangyang on 17/3/14.
 * 所有的接口都写在一个Interface中，方便管理
 */
public interface HttpService {
    @POST("category_list")
    Observable<HttpResult<String>> category_list();

    @GET("login")
    Observable<HttpResult<Title>> login(String user_name, String password);

    @GET("top250")
    Observable<HttpResult<String>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
