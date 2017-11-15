package cn.innovate.rfidservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;

public class UsbEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceApp app = ServiceApp.getInstance();
        app.d("UsbEventReceiver: " + intent.getAction());
        if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
            app.d("USB设备拔出: " + intent.getAction());
            RfidService.deviceDetached();
            System.exit(0);
        }
    }
}
