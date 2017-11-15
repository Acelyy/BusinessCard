package com.yonggang.liyangyang.businesscard.app_landscape.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class HttpUtil {
    //public static String BASE_URL = "http://192.168.202.71:83/Outsides/";
    // public static String BASE_URL = "http://192.168.23.1:83/Outsides/";
    //public static String BASE_URL = "http://192.168.6.1/Outsides/";
    public static String BASE_URL = "http://ylly.yong-gang.com/Outsides/";

    private Retrofit retrofit;

    private static HttpUtil INSTANCE = new HttpUtil();

    private static final Retrofit getRetrofit() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 私有化构造函数
     * 初始化retrofit对象
     */
    private HttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 统一设置观察者
     *
     * @param o
     * @param s
     * @param <T>
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);

    }

    /**
     * 用于返回单例
     *
     * @return
     */
    public static HttpUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!"100".equals(httpResult.getFlag())) {
                throw new ApiException(httpResult.getMsg());
            }
            return httpResult.getData();
        }
    }

    public static final <T> T getService(Class<T> clz) {
        return getRetrofit().create(clz);
    }

    /***************************
     * 设置各请求方法
     ***************************/

//    public <T> void login(Subscriber<User> subscriber, String user_name, String password, String device_no, String auth_code) {
//        Observable observable = login.doPost(user_name, password, device_no, auth_code)
//                .map(new HttpResultFunc<User>());
//        toSubscribe(observable, subscriber);
//    }

}
