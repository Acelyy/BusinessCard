package cn.innovate.rfidservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

import app.terminal.com.serialport.driver.UsbSerialPort;
import app.terminal.com.serialport.inter.BroadcastIntface;
import app.terminal.com.serialport.inter.ControlLinksilliconCardIntface;
import app.terminal.com.serialport.util.HexDump;
import app.terminal.com.serialport.util.SendByteData;
import app.terminal.com.serialport.util.SerialportControl;
import cn.innovate.rfidclientsdk.Consts;

public class RfidService extends Service {

    public static final int CONTROLER_INITIALIZED = 1;
    public static final int PORT_OPENNED = 2;
    public static final int DEVICE_OK = CONTROLER_INITIALIZED | PORT_OPENNED;

    public static final String READERID_NOT_ATTACHED = "未连接";

    private static ServiceApp app = ServiceApp.getInstance();
    private static UsbManager usbManager;
    private static ControlLinksilliconCardIntface rfidController = null;
    private static int status =0;
    private static boolean receiverRegistered = false;
    private static String deviceId = READERID_NOT_ATTACHED;

    private static final Object lock = new Object();

    private static final int baudRate = 9600;
    private static final int dataBits = 8;
    private static final int stopBits = UsbSerialPort.STOPBITS_1;
    private static final int parity = UsbSerialPort.PARITY_NONE;

    @Override
    public void onCreate() {
        super.onCreate();
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        app.d("读卡服务创建成功");
    }

    static class InstanceHolder {
        static RfidServiceImpl instance = new RfidServiceImpl();
    }

    public static class RfidServiceImpl extends IRfidService.Stub {

        @Override
        public int getStatus() throws RemoteException {
            return status;
        }


        @Override
        public int initialize() throws RemoteException {
            return initializeDevice();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return InstanceHolder.instance;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.d("读卡器服务关闭");
    }

    protected static void closeController() {
        try {
            rfidController.closeReader(usbManager);
        } catch (Exception e) {
        }
        rfidController = null;
        status = 0;
        deviceId = READERID_NOT_ATTACHED;
        app.d("关闭设备");
    }

    public static int initializeDevice() {
        synchronized (lock) {
            if (!receiverRegistered) {
                registerRfidReceiver();
                receiverRegistered = true;
            }
            if (usbManager == null) {
                usbManager = (UsbManager) app.getSystemService(Context.USB_SERVICE);
            }
            if (rfidController != null) {
                closeController();
            }
            status = 0;
            rfidController = new SerialportControl();
            try {
                rfidController.initSerialPort(usbManager);
                status |= CONTROLER_INITIALIZED;
                app.d("控制器初始化成功");
                if (rfidController.openReader(usbManager, baudRate, dataBits, stopBits, parity)) {
                    status |= PORT_OPENNED;
                    app.d("端口开启成功");
                    rfidController.getReaderId(app);
                } else {
                    app.d("端口开启失败");
                }
            } catch (IOException e) {
                app.d("控制器初始化失败: " + e.getMessage());
            }
        }
        return status;
    }

    private static BroadcastReceiver rfidReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            app.d("收到广播: " + action);
            if (action.equals(BroadcastIntface.GETREADERID_BROADCASTRECEIVER)) {
                byte[] responsedata = intent.getByteArrayExtra("RESPONSEDATA");
                byte[] sendData = intent.getByteArrayExtra("SENDDATA");
                updateReceivedData(sendData, responsedata);
            } else if (action.equals(Consts.RFID_QUERY_DEVICE_ACTION)) {
                if ("".equals(deviceId)) {
                    app.d("没有插入的设备，广播设备拔出消息");
                    deviceDetached();
                } else {
                    app.d("响应设备查询：" + deviceId );
                    deviceAttached(deviceId);
                }
            }

        }
    };

    private static boolean sameBytes(byte[] which, byte[] compareTo) {
        if (which.length == compareTo.length) {
            for(int i=0; i< which.length; i++) {
                if (which[i] != compareTo[i])
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private static String getCardId(byte[] data) {
        int startPos = (data.length == 5) ? 1:
                (data.length == 12) ? 8 : 0;
        if (startPos == 0)
            return "00000000";

        return new StringBuilder()
            .append(String.format("%02X",data[startPos+3]))
                .append(String.format("%02X",data[startPos+2]))
                .append(String.format("%02X",data[startPos+1]))
                .append(String.format("%02X",data[startPos]))
                .toString();
    }

    private static void broadCast(String type, String id) {
        Intent intent = new Intent(Consts.RFID_EVENT_ACTION);
        intent.putExtra(Consts.RFID_EVENT_TYPE, type);
        intent.putExtra(Consts.RFID_EVENT_DATA, id);
        app.sendBroadcast(intent);

    }

    public static void deviceAttached(String id) {
        app.d("读卡器插入,序列号: " + id);
        broadCast(Consts.RFID_EVENT_DEVICE_ATTACHED, id);
    }

    public static void cardDetected(String id) {
        app.d("自动寻卡: " + id);
        broadCast(Consts.RFID_EVENT_CARD_DETECT, id);
    }

    public static void deviceDetached() {
        app.d("读卡器拔出!");
        closeController();
        deviceId = "";
        broadCast(Consts.RFID_EVENT_DEVICE_DETACHED, deviceId);
    }

    private static void updateReceivedData(byte[] sendData, byte[] responseData) {
        if (sendData != null)
            app.d("发送数据: " + HexDump.toHexString(sendData));
        if (responseData != null)
            app.d("接收数据: " + HexDump.toHexString(responseData));
        if (responseData != null && sendData != null) {
                if (sameBytes(sendData, SendByteData.SERIAL_NUMBER_BYTE)) {
                    String carid = getCardId(responseData);
                     if(responseData.length == 12) {
                         deviceId = carid;
                         deviceAttached(carid);
                     } else {
                         cardDetected(carid);
                     }
                }
        }
    }

    private static void registerRfidReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastIntface.GETREADERID_BROADCASTRECEIVER);
        myIntentFilter.addAction(Consts.RFID_QUERY_DEVICE_ACTION);
        //注册广播
        app.registerReceiver(rfidReceiver, myIntentFilter);
        app.d("注册广播: " + myIntentFilter.getAction(0));
    }

    public static int getStatus() {
        return status;
    }

    public static String getDeviceId() {
        return deviceId;
    }
}
