package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.User;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.Activate;
import com.yonggang.liyangyang.businesscard.app_landscape.response.ActivateResult;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddDeviceActivity extends BaseActivity {

    @BindView(R.id.edit_device_no)
    EditText editDeviceNo;
    @BindView(R.id.edit_auth_code)
    EditText editAuthCode;

    User user;

    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        setTitle("永联旅游一卡通——收银系统");
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        user = app.getUser();
        if (user == null) {
            Toast.makeText(app, "登录信息失效，请退出重新登录", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        activateDevice(user.getUserid(), editDeviceNo.getText().toString(), editAuthCode.getText().toString());
    }

    private void activateDevice(String user_id, final String device_no, final String auth_code) {
        if ("".equals(device_no)) {
            Toast.makeText(this, "设备号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(auth_code)) {
            Toast.makeText(this, "激活码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("激活中");
        dialog.show();
        Activate ac = HttpUtil.getService(Activate.class);
        ac.doPost(device_no, auth_code, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivateResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(ActivateResult ac) {
                        Log.i("ac", ac.toString());
                        String flag = ac.getFlag();
                        if ("100".equals(flag)) {
                            Util.setDeviceNo(device_no, AddDeviceActivity.this);
                            Util.setAuthCode(auth_code, AddDeviceActivity.this);
                            Toast.makeText(AddDeviceActivity.this, "设备激活成功", Toast.LENGTH_SHORT).show();
                            app.setUser(ac.getUser());
                            goActivity(GoodsActivity.class);
                        } else {
                            Toast.makeText(AddDeviceActivity.this, ac.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

    }
}
