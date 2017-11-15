package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Goods;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.GetCardByCode;
import com.yonggang.liyangyang.businesscard.app_landscape.response.GetCardByCodeResult;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

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
        setTitle("永联旅游一卡通——收银系统");
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
                //goActivity(TestCardActivity.class);
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
                    //Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
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
                    public void onNext(GetCardByCodeResult gcbr) {
                        Log.i("s", gcbr.toString());
                        String flag = gcbr.getFlag();
                        if ("100".equals(flag)) {
                            Bundle bundle = new Bundle();
                            bundle.putDouble("sum", sum);
                            bundle.putSerializable("data", data);
                            bundle.putSerializable("card", gcbr.getCard());
                            bundle.putString("flag", "扫码");
                            stepActivity(bundle, PayActivity.class);
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