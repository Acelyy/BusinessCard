package cn.innovate.rfidservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceAttachedActivity extends Activity {
    private ServiceApp app = ServiceApp.getInstance();
    private int retry = 3;
    public static final int DELAY = 1000;

    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            app.d("初始化尝试: " + retry);
            Toast.makeText(app, "开始初始化设备",Toast.LENGTH_LONG).show();
            //int state = 1;
            int state = RfidService.initializeDevice();
            app.d("设备初始化结构=" + state);
            if (state == RfidService.DEVICE_OK || retry == 0){
                DeviceAttachedActivity.this.finish();
            } else {
                retry--;
                delayInit();
            }
        }
    };

    protected void delayInit() {
        new Handler().postDelayed(timerTask, DELAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.d("检测到读卡器");
        delayInit();
    }


}
