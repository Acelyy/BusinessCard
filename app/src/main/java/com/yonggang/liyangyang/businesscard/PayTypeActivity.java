package com.yonggang.liyangyang.businesscard;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.Entry.Goods;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.request.GetCardByCode;
import com.yonggang.liyangyang.businesscard.response.GetCardByCodeResult;
import com.yonggang.liyangyang.businesscard.util.Util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayTypeActivity extends BaseActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private double sum;
    private ArrayList<Goods> data;

    FinishBroadcast finishBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_type);
        ButterKnife.bind(this);
        sum = getIntent().getExtras().getDouble("sum");
        data = (ArrayList<Goods>) getIntent().getExtras().getSerializable("data");
        //注册广播
        finishBroadcast = new FinishBroadcast();
        IntentFilter filter = new IntentFilter("finish");
        registerReceiver(finishBroadcast, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finishBroadcast);
        super.onDestroy();
    }

    @OnClick({R.id.btn_pay_card, R.id.btn_pay_qrCode, R.id.btn_pay_cash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay_card:
                Bundle bundle = new Bundle();
                bundle.putDouble("sum", sum);
                bundle.putSerializable("data", data);
                stepActivity(bundle, CardActivity.class);
                break;
            case R.id.btn_pay_qrCode:
                Intent intent = new Intent();
                intent.setClass(PayTypeActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                //getCardByCode("149022843779931986");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String code = bundle.getString("result");
                    getCardByCode(code);
                    Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 根据二维码查找卡信息
     *
     * @param code
     */
    private void getCardByCode(String code) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("获取信息中");
        dialog.show();
        GetCardByCode gcb = HttpUtil.getService(GetCardByCode.class);
        gcb.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCardByCodeResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("e", e.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(final GetCardByCodeResult gcbr) {
                        Log.i("s", gcbr.toString());
                        String flag = gcbr.getFlag();
                        if ("100".equals(flag)) {
                            final String pwd = gcbr.getCard().getCard_pwd();
                            if ("".equals(pwd)) {
                                Bundle bundle = new Bundle();
                                bundle.putDouble("sum", sum);
                                bundle.putSerializable("data", data);
                                bundle.putSerializable("card", gcbr.getCard());
                                bundle.putString("flag", "扫码");
                                goActivity(bundle, PayActivity.class);
                            } else {
                                View view = LayoutInflater.from(PayTypeActivity.this).inflate(R.layout.item_pwd, null);
                                final EditText editText = (EditText) view.findViewById(R.id.input);
                                AlertDialog.Builder builder = new AlertDialog.Builder(PayTypeActivity.this);
                                builder.setTitle("请输入密码")
                                        .setView(view)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String msg = editText.getText().toString().trim();
                                                if (msg.equals(pwd)) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putDouble("sum", sum);
                                                    bundle.putSerializable("data", data);
                                                    bundle.putSerializable("card", gcbr.getCard());
                                                    bundle.putString("flag", "扫码");
                                                    goActivity(bundle, PayActivity.class);
                                                } else {
                                                    Toast.makeText(PayTypeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create().show();
                            }
                        } else {
                            Toast.makeText(PayTypeActivity.this, gcbr.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

    }

    class FinishBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}