package com.yonggang.liyangyang.businesscard;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.print.Global;
import com.print.WorkService;
import com.utils.DataUtils;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintMSGActivity extends AppCompatActivity {

    private static Handler mHandler = null;

    private String msg;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_msg);
        ButterKnife.bind(this);
        msg = getIntent().getExtras().getString("msg");
        dialog = new ProgressDialog(this);
        dialog.show();
        /* 启动蓝牙 */
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null != adapter) {
            if (!adapter.isEnabled()) {
                if (adapter.enable()) {
                    // while(!adapter.isEnabled());
                    Log.v("SearchActivity",
                            "Enable BluetoothAdapter");
                } else {
                    return;
                }
            }
        }
        mHandler = new MHandler(PrintMSGActivity.this);
        WorkService.addHandler(mHandler);
        if (null == WorkService.workThread) {
            Intent intent = new Intent(PrintMSGActivity.this, WorkService.class);
            startService(intent);
        }
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_print, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                print(msg);
                break;
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.setAction("finish");
                sendBroadcast(intent);
                finish();
                break;
        }
    }

    // 打印订单方法
    private void print(String text) {
        if (WorkService.workThread.isConnected()) {
            byte header[] = null;
            byte strbuf[] = null;
            // 设置UTF8编码
            // Android手机默认也是UTF8编码
            header = new byte[]{0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x39,
                    0x01};
            try {
                strbuf = text.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            byte buffer[] = DataUtils.byteArraysToBytes(new byte[][]{
                    header, strbuf});
            Bundle data = new Bundle();
            data.putByteArray(Global.BYTESPARA1, buffer);
            data.putInt(Global.INTPARA1, 0);
            data.putInt(Global.INTPARA2, buffer.length);
            WorkService.workThread.handleCmd(Global.CMD_POS_WRITE, data);
        } else {
            Intent intent = new Intent(PrintMSGActivity.this,
                    PrintActivity.class);
            startActivity(intent);
        }
    }

    class MHandler extends Handler {

        WeakReference<PrintMSGActivity> mActivity;

        MHandler(PrintMSGActivity activity) {
            mActivity = new WeakReference<PrintMSGActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PrintMSGActivity theActivity = mActivity.get();
            Log.i("printFlag", msg.what + "");
            switch (msg.what) {
                case Global.CMD_POS_WRITERESULT:
                    int result1 = msg.arg1;
                    Log.i("result1", result1 + "");
                    if (result1 == 1) {
                        Toast.makeText(theActivity, "打印成功", Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent();
                        intent.setAction("finish");
                        sendBroadcast(intent);
                        finish();
                    } else {
                        Toast.makeText(theActivity, "与打印机断开连接，请重新尝试连接",
                                Toast.LENGTH_SHORT).show();
                        WorkService.workThread.disconnectBt();
                    }
                    break;
                default:
                    break;
            }

        }
    }
}
