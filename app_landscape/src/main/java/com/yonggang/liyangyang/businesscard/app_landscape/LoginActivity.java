package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.User;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.GetVersion;
import com.yonggang.liyangyang.businesscard.app_landscape.request.LoginSever;
import com.yonggang.liyangyang.businesscard.app_landscape.response.LoginResult;
import com.yonggang.liyangyang.businesscard.app_landscape.response.Version;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Constants;
import com.yonggang.liyangyang.businesscard.app_landscape.util.DownLoadRunnable;
import com.yonggang.liyangyang.businesscard.app_landscape.util.MD5;
import com.yonggang.liyangyang.businesscard.app_landscape.util.MyUtils;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pass)
    EditText editPass;

    MyApplication app;
    @BindView(R.id.device_no)
    TextView deviceNo;
    @BindView(R.id.version_code)
    TextView versionCode;

    //handler更新ui
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(LoginActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    install(LoginActivity.this);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("永联旅游一卡通——收银系统");
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        deviceNo.setText(Util.getDeviceNo(this));
        editName.setText(Util.getUserName(this));
        editPass.setText(Util.getPassword(this));
        versionCode.setText(GetVersion(this));
        //getVersion();
        //Util.setDeviceNo("03", this);
        //Util.setAuthCode("aUtxta6V", this);
    }

    @OnClick({R.id.btn_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
                //goActivity(GoodsActivity.class);
                //HttpUtil.getInstance().login(new ProgressSubscriber<User>(loginOnNext, this), editName.getText().toString(), editPass.getText().toString(), Util.getDeviceNo(this), Util.getAuthCode(this));
                login(editName.getText().toString(), editPass.getText().toString());
                break;
        }
    }

    /**
     * 登录方法
     *
     * @param userName
     * @param password
     */
    private void login(final String userName, final String password) {
        if ("".equals(userName)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("登录中");
        dialog.show();
        final LoginSever login = HttpUtil.getService(LoginSever.class);
        login.doPost(userName, MD5.GetMD5Code(password), Util.getDeviceNo(this), Util.getAuthCode(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                        Log.i("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        Log.i("login", loginResult.toString());
                        String flag = loginResult.getFlag();
                        if ("001".equals(flag)) {
                            Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                        } else if ("002".equals(flag)) {
                            Toast.makeText(LoginActivity.this, "该设备尚未激活", Toast.LENGTH_SHORT).show();
                            User user = loginResult.getUser();
                            if (user != null) {
                                app.setUser(loginResult.getUser());
                                goActivity(AddDeviceActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "登录信息有误，请尝试重新登录", Toast.LENGTH_SHORT).show();
                            }

                        } else if ("100".equals(flag)) {
                            app.setUser(loginResult.getUser());
                            Util.setSharedUser(userName, password, LoginActivity.this);
                            goActivity(GoodsActivity.class);
                        } else {
                            Toast.makeText(LoginActivity.this, loginResult.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        GetVersion getVersion = HttpUtil.getService(GetVersion.class);
        getVersion.doPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Version>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final Version version) {
                        String version_new = version.getVersion();
                        String version_local = GetVersion(LoginActivity.this);
                        if ("".equals(version_new) || "".equals(version_local)) {
                            return;
                        }
                        if (Double.parseDouble(version_new) > Double.parseDouble(version_local)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("有新版本，是否更新")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String url = version.getFile();
                                            //String url = "http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk";
                                            new Thread(new DownLoadRunnable(url, 0, handler)).start();
                                        }
                                    }).setNegativeButton("取消", null)
                                    .create().show();
                        }
                    }
                });
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public void install(Context context) {
        Log.i("install", "start");
        File file = MyUtils.getCacheFile(Constants.APPNAME);
        if (file == null || !file.exists()) {
            return;
        }
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(installintent);
        Log.i("install", "finish");
    }
}
