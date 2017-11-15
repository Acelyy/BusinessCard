package com.yonggang.liyangyang.businesscard;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.print.Global;
import com.print.WorkService;
import com.utils.TimeUtils;

public class PrintActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mHandler = null;

    private LinearLayout linearlayoutdevices;
    private ProgressDialog dialog;

    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;

    private TextView text_device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initBroadcast();
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
        mHandler = new MHandler();
        WorkService.addHandler(mHandler);
        if (null == WorkService.workThread) {
            Intent intent = new Intent(this, WorkService.class);
            startService(intent);
        }
        text_device = (TextView) findViewById(R.id.text_device);
        findViewById(R.id.buttonSearch).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        WorkService.delHandler(mHandler);
        uninitBroadcast();
        mHandler = null;
        super.onDestroy();
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    final String address = device.getAddress();
                    String name = device.getName();
                    if (name == null)
                        name = "BT";
                    else if (name.equals(address))
                        name = "BT";
                    Button button = new Button(context);
                    button.setText(name + ": " + address);
                    button.setTextColor(Color.BLACK);
                    button.setTextSize(15);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL
                            | Gravity.LEFT);
                    // button.setLayoutParams(new
                    // LayoutParams(LayoutParams.MATCH_PARENT,30));
                    button.setBackgroundColor(Color.WHITE);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {

                            WorkService.workThread.disconnectBt();

                            WorkService.workThread.connectBt(address);
                        }
                    });
                    linearlayoutdevices.addView(button);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    // progressBarSearchStatus.setIndeterminate(true);
                    dialog = new ProgressDialog(PrintActivity.this);
                    dialog.show();
                    text_device.setVisibility(View.VISIBLE);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    class MHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                /**
                 * DrawerService 的 onStartCommand会发送这个消息
                 */
                case Global.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
                    int result = msg.arg1;
                    // Log.v(TAG, "Connect Result: " + result);
                    // theActivity.dialog.cancel();
                    if (1 == result) {
                        Toast.makeText(getApplicationContext(), "连接成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSearch:
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    break;
                }

                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        // Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        break;
                    }
                }

                if (null != WorkService.workThread) {
                    WorkService.workThread.disconnectBt();
                    TimeUtils.WaitMs(10);
                }
                adapter.cancelDiscovery();
                linearlayoutdevices.removeAllViews();
                TimeUtils.WaitMs(10);
                adapter.startDiscovery();
                break;
        }
    }

}
