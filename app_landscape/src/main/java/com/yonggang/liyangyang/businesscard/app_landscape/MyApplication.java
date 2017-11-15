package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.Application;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.User;


/**
 * Created by liyangyang on 2017/3/22.
 */

public class MyApplication extends Application {
    private static MyApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
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
