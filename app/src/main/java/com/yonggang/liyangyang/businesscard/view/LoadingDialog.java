package com.yonggang.liyangyang.businesscard.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.yonggang.liyangyang.businesscard.R;

/**
 * Created by liyangyang on 2017/3/17.
 */

public class LoadingDialog extends AlertDialog {
    private TextView tips_loading_msg;
    private int layoutResId;
    private String message = null;

    /**
     * 构造方法
     *
     * @param context
     */
    public LoadingDialog(Context context, String text) {
        super(context);
        this.layoutResId = R.layout.view_tips_loading;
        message = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResId);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);
    }
}
