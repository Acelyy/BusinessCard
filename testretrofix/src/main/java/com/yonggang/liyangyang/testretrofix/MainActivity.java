package com.yonggang.liyangyang.testretrofix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yonggang.liyangyang.testretrofix.httpUtil.HttpUtil;
import com.yonggang.liyangyang.testretrofix.httpUtil.ProgressSubscriber;
import com.yonggang.liyangyang.testretrofix.httpUtil.SubscriberOnNextListener;

import static com.yonggang.liyangyang.testretrofix.R.id.login;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SubscriberOnNextListener onNext=new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("s", s);
            }
        };
        HttpUtil.getInstance().getTopMovie(new ProgressSubscriber(onNext, MainActivity.this),0,10);
        //HttpUtil.getInstance().login(new ProgressSubscriber(onNext, MainActivity.this, "获取中"));
    }
}
