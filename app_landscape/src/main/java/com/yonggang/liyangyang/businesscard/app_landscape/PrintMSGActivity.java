package com.yonggang.liyangyang.businesscard.app_landscape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class PrintMSGActivity extends AppCompatActivity {


    private String msg;

    public static String MODULE_FLAG = "module_flag";
    public static int module_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_msg);
        ButterKnife.bind(this);
    }

}
