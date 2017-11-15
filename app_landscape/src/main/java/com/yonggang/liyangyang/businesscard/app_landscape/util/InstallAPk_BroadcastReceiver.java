package com.yonggang.liyangyang.businesscard.app_landscape.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * 用途：安装apk
 */
public class InstallAPk_BroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        install(context);
    }

    public void install(Context context) {
        Log.i("install", "start");
        File file = MyUtils.getCacheFile(Constants.APPNAME);
        if (file == null || !file.exists()) {
            return;
        }
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(installintent);
        Log.i("install", "finish");
    }
}
