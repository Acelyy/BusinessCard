package com.yonggang.liyangyang.businesscard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.innovate.rfidclientsdk.RFIDCLient;

/**
 * Created by liyangyang on 2017/3/17.
 */

public class BaseActivity extends AppCompatActivity implements  RFIDCLient.RfidEventListener{
    protected String rfidDevice = null;
    protected RFIDCLient rfidClient = new RFIDCLient();

    @Override
    protected void onStart() {
        super.onStart();
        rfidClient.start(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rfidClient.stop();
    }

    protected void goActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        this.finish();
    }

    protected void goActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    protected void stepActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    protected void stepActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void deviceAttached(String deviceId) {
        rfidDevice = deviceId;
    }

    @Override
    public void deviceDetached() {
        rfidDevice = null;
    }

    @Override
    public void cardDetected(String cardId) {

    }
}
