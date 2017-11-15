package com.yonggang.liyangyang.businesscard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.Entry.Data;
import com.yonggang.liyangyang.businesscard.Entry.Goods;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.request.GetTrade;
import com.yonggang.liyangyang.businesscard.response.OrderDetailResult;
import com.yonggang.liyangyang.businesscard.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity {

    @BindView(R.id.list_order)
    ListView listOrder;
    @BindView(R.id.sum_price)
    TextView sumPrice;
    @BindView(R.id.total_price)
    TextView totalPrice;

    List<Goods> data = new ArrayList<Goods>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        String trade_id = getIntent().getExtras().getString("trade_id");
        getTrade(trade_id);
    }


    private void getTrade(String trade_id) {
        GetTrade getTrade = HttpUtil.getService(GetTrade.class);
        getTrade.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), trade_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(OrderDetailResult s) {
                        Log.i("s", s.toString());
                        String flag = s.getFlag();
                        if ("100".equals(flag)) {
                            sumPrice.setText(s.getData().getTrade_money());
                            totalPrice.setText(s.getData().getDiscount_money());
                            data = s.getData().getItem();
                            Log.i("s", JSON.toJSONString(data));
                            listOrder.setAdapter(new OrderAdapter());

                        } else {
                            Toast.makeText(OrderDetailActivity.this, s.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    class OrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_orderdetail, null);
                holder.detail_back = (LinearLayout) convertView.findViewById(R.id.detail_back);
                holder.detail_name = (TextView) convertView.findViewById(R.id.detail_name);
                holder.detail_num = (TextView) convertView.findViewById(R.id.detail_num);
                holder.detail_price = (TextView) convertView.findViewById(R.id.detail_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position % 2 == 0) {
                holder.detail_back.setBackgroundColor(Color.parseColor("#F1F1F1"));
            } else {
                holder.detail_back.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            holder.detail_name.setText(data.get(position).getGood_name());
            holder.detail_num.setText("X" + data.get(position).getAmount());
            holder.detail_price.setText("￥" + data.get(position).getMoney());
            return convertView;
        }
    }

    class ViewHolder {
        LinearLayout detail_back;
        TextView detail_name;
        TextView detail_num;
        TextView detail_price;
    }
}
