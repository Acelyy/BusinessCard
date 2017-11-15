package com.yonggang.liyangyang.businesscard.app_landscape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by liyangyang on 2017/3/17.
 */

public class BaseActivity extends AppCompatActivity {

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

}
