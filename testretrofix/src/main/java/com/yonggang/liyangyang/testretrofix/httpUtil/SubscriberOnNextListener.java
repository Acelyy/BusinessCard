package com.yonggang.liyangyang.testretrofix.httpUtil;

/**
 * Created by liyangyang on 17/4/10.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}