package com.yonggang.liyangyang.businesscard.app_landscape;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonggang.liyangyang.businesscard.app_landscape.Entry.User;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.EditUser;
import com.yonggang.liyangyang.businesscard.app_landscape.util.MD5;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePassActivity extends BaseActivity {

    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_new_pass)
    EditText editNewPass;
    @BindView(R.id.edit_config_pass)
    EditText editConfigPass;

    MyApplication app;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
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
        changePass(editPassword.getText().toString().trim(), editNewPass.getText().toString().trim(), editConfigPass.getText().toString().trim());
    }

    private void changePass(String pass, String new_pass, String config_pass) {
        if ("".equals(pass)) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(pass)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_pass.equals(pass)) {
            Toast.makeText(this, "新密码与原密码不能相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new_pass.equals(config_pass)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        EditUser editUser = HttpUtil.getService(EditUser.class);
        editUser.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), MD5.GetMD5Code(pass), MD5.GetMD5Code(new_pass), user.getUserid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("s", s);
                        JSONObject json = JSON.parseObject(s);
                        String flag = json.getString("flag");
                        if ("100".equals(flag)) {
                            Util.setSharedUser("", "", ChangePassActivity.this);

                            Intent intent = new Intent("exit");
                            sendBroadcast(intent);
                            goActivity(LoginActivity.class);
                        } else {
                            Toast.makeText(ChangePassActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
