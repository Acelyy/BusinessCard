package com.yonggang.liyangyang.businesscard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonggang.liyangyang.businesscard.Entry.Seller;
import com.yonggang.liyangyang.businesscard.request.SetInfo;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.edit_tel)
    EditText editTel;
    @BindView(R.id.edit_discount)
    TextView editDiscount;

    private Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        seller = (Seller) getIntent().getExtras().getSerializable("seller");
        if (seller == null) {
            Toast.makeText(this, "登录信息失效，请退出重新登录", Toast.LENGTH_SHORT).show();
            finish();
        }
        editTel.setText(seller.getPhone());
        editDiscount.setText(seller.getDiscount());
    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        saveInfo(editTel.getText().toString(), Double.parseDouble(editDiscount.getText().toString()));
    }

    private void saveInfo(String phone, double discount) {
        if (discount > 1) {
            Toast.makeText(this, "折扣应在0.1~1之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (discount <= 0) {
            Toast.makeText(this, "折扣应在0.1~1之间", Toast.LENGTH_SHORT).show();
            return;
        }
        SetInfo setInfo = HttpUtil.getService(SetInfo.class);
        setInfo.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), phone, String.valueOf(discount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        JSONObject object = JSON.parseObject(s);
                        String flag = object.getString("flag");
                        if ("100".equals(flag)) {
                            Toast.makeText(SettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SettingActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
