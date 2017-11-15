package com.yonggang.liyangyang.businesscard.request;

import com.yonggang.liyangyang.businesscard.response.Version;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 2017/3/26.
 */

public interface GetVersion {
    @POST("getVersion")
    Observable<Version> doPost();
}
