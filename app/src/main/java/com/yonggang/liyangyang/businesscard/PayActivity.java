package com.yonggang.liyangyang.businesscard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonggang.liyangyang.businesscard.Entry.Card;
import com.yonggang.liyangyang.businesscard.Entry.Goods;
import com.yonggang.liyangyang.businesscard.Entry.User;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.request.Pay;
import com.yonggang.liyangyang.businesscard.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayActivity extends BaseActivity {

    @BindView(R.id.txt_card_id)
    TextView txtCardId;
    @BindView(R.id.txt_lest_money)
    TextView txtLestMoney;
    @BindView(R.id.txt_pay_money)
    TextView txtPayMoney;
    @BindView(R.id.txt_discount_money)
    TextView txtDiscountMoney;
    @BindView(R.id.txt_pay_cash)
    TextView txtPayCash;
    @BindView(R.id.txt_rest_money)
    TextView txtRestMoney;
    @BindView(R.id.discount)
    TextView discount;


    private Card card;
    private double sum;
    private double sum_discount;

    DecimalFormat df = new DecimalFormat("#0.0");

    ArrayList<Goods> data;

    private String flag;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    MyApplication app;
    User user;

    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        user = app.getUser();
        df.setRoundingMode(RoundingMode.DOWN);
        app = (MyApplication) getApplication();
        user = app.getUser();
        if (user == null) {
            Toast.makeText(app, "登录信息失效，请退出重新登录", Toast.LENGTH_SHORT).show();
            finish();
        }
        card = (Card) getIntent().getExtras().getSerializable("card");
        sum = getIntent().getExtras().getDouble("sum");
        data = (ArrayList<Goods>) getIntent().getExtras().getSerializable("data");
        flag = getIntent().getExtras().getString("flag");
        sum_discount = sum * card.getDiscount();
        switch (String.valueOf(card.getDiscount())) {
            case "1.0":
                msg = "不打折";
                break;
            default:
                msg = card.getDiscount() * 10 + "折";
                break;
        }
        discount.setText(msg);
        txtCardId.setText(card.getCard_num());
        //double lest = card.getRecharge_money() + card.getDonation_money() + card.getTransfer_money() - card.getConsume_money();
        BigDecimal recharge_money = new BigDecimal(Double.toString(card.getRecharge_money()));
        BigDecimal donation_money = new BigDecimal(Double.toString(card.getDonation_money()));
        BigDecimal transfer_money = new BigDecimal(Double.toString(card.getTransfer_money()));
        BigDecimal consume_money = new BigDecimal(Double.toString(card.getConsume_money()));

        BigDecimal b1 = recharge_money.add(donation_money);
        BigDecimal b2 = b1.add(transfer_money);
        BigDecimal b3 = b2.subtract(consume_money);
        //System.out.println(b3.doubleValue());
        double lest = b3.doubleValue();
        txtLestMoney.setText(df.format(lest) + "元");
        txtPayMoney.setText(df.format(sum) + "元");
        txtDiscountMoney.setText("-" + df.format(Double.parseDouble(df.format(sum)) - Double.parseDouble(df.format(sum_discount))) + "元");
        txtPayCash.setText(df.format(sum_discount) + "元");
        txtRestMoney.setText(df.format(card.getRecharge_money() + card.getDonation_money() + card.getTransfer_money() - card.getConsume_money() - sum_discount) + "元");

    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        pay();
    }

    /**
     * 确认支付
     */
    private void pay() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("获取信息中");
        dialog.show();
        Pay pay = HttpUtil.getService(Pay.class);
        pay.doPost(
                Util.getDeviceNo(this),//设备号
                Util.getAuthCode(this),//激活码
                card.getCard_sn(),//card_sn
                subYuan(txtPayMoney.getText().toString()),//实际价格
                card.getDiscount() + "",//折扣
                subYuan(txtPayCash.getText().toString()),//折后金额
                flag,//刷卡或者扫码
                "",//备注
                user.getUsername(),//用户名
                JSON.toJSONString(data)//商品详情
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("s", s);
                        JSONObject object = JSON.parseObject(s);
                        String flag = object.getString("flag");
                        if ("100".equals(flag)) {
                            Toast.makeText(PayActivity.this, "消费成功", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                            builder.setTitle("是否需要打印小票？")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("msg", getString());
                                            goActivity(bundle, PrintMSGActivity.class);
                                        }
                                    })
                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction("finish");
                                            sendBroadcast(intent);
                                            finish();
                                        }
                                    }).setCancelable(false).create().show();
                        } else {
                            Toast.makeText(PayActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 编辑打印信息
     *
     * @return
     */
    private String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append("     永联一卡通结算小票");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("时间:");
        sb.append(sdf.format(new Date()));
        sb.append("\r\n");
        sb.append("设备号:");
        sb.append(Util.getDeviceNo(this));
        sb.append("\r\n");
        sb.append("卡号:");
        sb.append(txtCardId.getText());
        sb.append("\r\n");
        sb.append("收银员:");
        sb.append(user.getUsername());
        sb.append("\r\n");
        if (data.size() > 0) {
            sb.append("--------------------------------\r\n");
            sb.append("名称       数量    单价     金额\r\n");
            sb.append("--------------------------------\r\n");
            for (Goods goods : data) {
                String goodname = goods.getGood_name();
                String outamount = goods.getAmount() + "";
                String outprice = df.format(goods.getPrice()) + "";
                String outmoney = df.format(goods.getMoney()) + "";
                sb.append(goodname);
                sb.append("      ");
                sb.append(outamount);
                for (int i = 0; i < 6 - outamount.length(); i++) {
                    sb.append(" ");
                }
                sb.append(outprice);
                for (int i = 0; i < 7 - outprice.length(); i++) {
                    sb.append(" ");
                }
                sb.append(outmoney);
                sb.append("\r\n");
            }
        }
        sb.append("--------------------------------\r\n");
        sb.append("消费金额:");
        sb.append(txtPayMoney.getText().toString());
        sb.append("\r\n");
        sb.append("折扣:");
        sb.append(msg);
        sb.append("\r\n");
        sb.append("实付金额:");
        sb.append(txtPayCash.getText().toString());
        sb.append("\r\n");
        sb.append("余额:");
        sb.append(txtRestMoney.getText().toString());
        sb.append("\r\n");
        sb.append("--------------------------------\r\n");
        sb.append("     谢谢惠顾，欢迎下次光临     ");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    /**
     * @param yuan
     * @return
     */
    private String subYuan(String yuan) {
        if (yuan.endsWith("元")) {
            return yuan.substring(0, yuan.length() - 1);
        } else {
            return yuan;
        }
    }
}
