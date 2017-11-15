package com.yonggang.liyangyang.businesscard.util;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yonggang.liyangyang.businesscard.MyApplication;

import java.io.File;


public class MyUtils {

    /**
     * 获得存储文件
     *
     * @param
     * @param
     * @return
     */
    public static File getCacheFile(String name) {
        String cachePath;
        Context context= MyApplication.getAppContext();
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {

            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + name);
    }

    /**
     * 获取手机大小，px
     * @param context
     * @return
     */
    public static DisplayMetrics getPhoneMetrics(Context context) {// 获取手机分辨率
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
