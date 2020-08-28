package com.yonggang.liyangyang.testretrofix.httpUtil;

import android.util.Log;

import com.yonggang.liyangyang.testretrofix.response.HttpResult;
import com.yonggang.liyangyang.testretrofix.response.Title;

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

    //public static String BASE_URL = "http://192.168.202.141:85/";
    //public static String BASE_URL = "http://www.ylcai.com/Outsides/";
    //public static String BASE_URL = "http://192.168.201.58/zhyl/index.php/Home/Index/";
    public static String BASE_URL = "https://api.douban.com/v2/movie/";
    public static int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private HttpService httpService;

    private static HttpUtil INSTANCE = new HttpUtil();

    private HttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpUtil getInstance() {
        return INSTANCE;
    }


    /**
     * 用来统一处理Http的flag,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.i("result", httpResult.toString());
            if (httpResult.getFlag() == 0) {
                String msg = httpResult.getMsg();
                if (msg == null) {
                    msg = "msg为空";
                }
                throw new RuntimeException(msg);
            }
            return httpResult.getData();
        }
    }

    /**
     * 统一配置观察者
     *
     * @param o
     * @param <T>
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void login(Subscriber<HttpResult<String>> subscriber) {
        Observable observable = httpService.category_list()
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    public void login2(Subscriber<HttpResult<Title>> subscriber, String user_name, String password) {
        Observable observable = httpService.login(user_name, password)
                .map(new HttpResultFunc<Title>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param start      起始位置
     * @param count      获取长度
     */
    public void getTopMovie(Subscriber<String> subscriber, int start, int count) {

//        movieService.getTopMovie(start, count)
//                .map(new HttpResultFunc<List<Subject>>())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

        Observable observable = httpService.getTopMovie(start, count)
                .map(new HttpResultFunc<String>());

        toSubscribe(observable, subscriber);
    }

}
