package com.yonggang.liyangyang.businesscard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.Entry.Goods;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.request.GetCardBySn;
import com.yonggang.liyangyang.businesscard.response.GetCardByCodeResult;
import com.yonggang.liyangyang.businesscard.util.MD5;
import com.yonggang.liyangyang.businesscard.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardActivity extends BaseActivity {

    @BindView(R.id.edit_card_id)
    TextView editCardId;

    double sum;
    ArrayList<Goods> data;

    private String card_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        //getWindow().setSoftInputMode(   WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //editCardId.setInputType(InputType.TYPE_NULL);
        sum = getIntent().getExtras().getDouble("sum");
        data = (ArrayList<Goods>) getIntent().getExtras().getSerializable("data");
    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        if (card_id == null || "".equals(card_id)) {
            Toast.makeText(this, "卡号为空", Toast.LENGTH_SHORT).show();
            return;
        }
        getCardBySn(Long.parseLong(card_id));
    }

    /**
     * @param sn
     */
    private void getCardBySn(long sn) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("获取中");
        dialog.show();
        final GetCardBySn gcb = HttpUtil.getService(GetCardBySn.class);
        gcb.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), MD5.GetMD5Code(sn + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCardByCodeResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("e", e.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(final GetCardByCodeResult gcbr) {
                        Log.i("s", gcbr.toString());
                        String flag = gcbr.getFlag();
                        if ("100".equals(flag)) {
                            final String pwd = gcbr.getCard().getCard_pwd();
                            if ("".equals(pwd)) {
                                Bundle bundle = new Bundle();
                                bundle.putDouble("sum", sum);
                                bundle.putSerializable("data", data);
                                bundle.putSerializable("card", gcbr.getCard());
                                bundle.putString("flag", "刷卡");
                                goActivity(bundle, PayActivity.class);
                            } else {
                                View view = LayoutInflater.from(CardActivity.this).inflate(R.layout.item_pwd, null);
                                final EditText editText = (EditText) view.findViewById(R.id.input);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                                builder.setTitle("请输入密码")
                                        .setView(view)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String msg = editText.getText().toString().trim();
                                                if (msg.equals(pwd)) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putDouble("sum", sum);
                                                    bundle.putSerializable("data", data);
                                                    bundle.putSerializable("card", gcbr.getCard());
                                                    bundle.putString("flag", "刷卡");
                                                    goActivity(bundle, PayActivity.class);
                                                } else {
                                                    Toast.makeText(CardActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create().show();
                            }

                        } else {
                            Toast.makeText(CardActivity.this, gcbr.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void cardDetected(String cardId) {
        super.cardDetected(cardId);
        //处理卡号，将16进制的卡号转化为10进制
        long cardId10 = Long.parseLong(cardId, 16);
        card_id = cardId10 + "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < card_id.length(); i++) {
            sb.append("*");
        }
        editCardId.setText(sb.toString());
    }
}
