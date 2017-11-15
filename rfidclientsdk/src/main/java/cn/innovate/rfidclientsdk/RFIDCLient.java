package cn.innovate.rfidclientsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class RFIDCLient {
    public interface RfidEventListener {
        void deviceAttached(String deviceId);
        void deviceDetached();
        void cardDetected(String cardId);
    }

    private RfidEventListener listener = null;
    private boolean started = false;
    private Context context;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (listener != null) {
                if (Consts.RFID_EVENT_ACTION.equals(intent.getAction())) {
                    String type = intent.getStringExtra(Consts.RFID_EVENT_TYPE);
                    String data = intent.getStringExtra(Consts.RFID_EVENT_DATA);
                    if (Consts.RFID_EVENT_DEVICE_ATTACHED.equals(type)) {
                        listener.deviceAttached(data);
                        return;
                    }

                    if (Consts.RFID_EVENT_CARD_DETECT.equals(type)) {
                        listener.cardDetected(data);
                        return;
                    }

                    if (Consts.RFID_EVENT_DEVICE_DETACHED.equals(type)){
                        listener.deviceDetached();
                    }
                }
            }
        }
    };

    public void start(Context context, RfidEventListener listener) {
        if (!started) {
            this.context = context;
            this.listener = listener;
            IntentFilter readerFilter = new IntentFilter(Consts.RFID_EVENT_ACTION);
            context.registerReceiver(receiver,readerFilter);

            /*
             * 查询当前RFID服务是否有设备连接，RFID服务发送广播
             * RFID_EVENT_DEVICE_ATTACHED或RFID_EVENT_DEVICE_DETACHED作为回复
             */
            Intent queryIntent = new Intent(Consts.RFID_QUERY_DEVICE_ACTION);
            context.sendBroadcast(queryIntent);
            started = true;
        }
    }

    public void stop() {
        if (started) {
            context.unregisterReceiver(receiver);
            this.listener = null;
            this.context = null;
            this.started = false;
        }
    }
}
