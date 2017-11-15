package com.yonggang.liyangyang.businesscard;

import android.app.Application;

import com.yonggang.liyangyang.businesscard.Entry.User;
import com.yonggang.liyangyang.businesscard.util.LogcatHelper;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class MyApplication extends Application {
    private static MyApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LogcatHelper.getInstance(getApplicationContext()).start();
        appContext = this;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static MyApplication getAppContext() {
        return appContext;
    }

}
