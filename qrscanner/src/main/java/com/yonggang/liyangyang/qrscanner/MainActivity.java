package com.yonggang.liyangyang.qrscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private TextView txt_result;
    private ImageView img_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_result = (TextView) findViewById(R.id.txt_result);
        img_result = (ImageView) findViewById(R.id.img_result);
        findViewById(R.id.btn_scanner).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //跳转至扫二维码界面
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
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
                    txt_result.setText(code);
                    Bitmap bitmap = bundle.getParcelable("bitmap");
                    img_result.setImageBitmap(bitmap);
                }
                break;
        }
    }
}
