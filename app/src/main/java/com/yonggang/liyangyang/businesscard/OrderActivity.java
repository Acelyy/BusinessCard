package com.yonggang.liyangyang.businesscard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yonggang.liyangyang.businesscard.Entry.Goods;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.sum_price)
    TextView sumPrice;
    @BindView(R.id.is_discount)
    CheckBox isDiscount;
    @BindView(R.id.list_order)
    ListView list_order;

    ArrayList<Goods> data = new ArrayList<Goods>();
    OrderAdapter adapter;
    double sum;
    DecimalFormat df = new DecimalFormat("#.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        df.setRoundingMode(RoundingMode.DOWN);
        data = (ArrayList<Goods>) getIntent().getExtras().getSerializable("goods_list");
        sum = getIntent().getExtras().getDouble("sum");
        sumPrice.setText(df.format(sum) + "元");
        adapter = new OrderAdapter();
        list_order.setAdapter(adapter);
    }

    @OnClick(R.id.btn_pay_cash)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putDouble("sum", sum);
        bundle.putSerializable("data", data);
        goActivity(bundle, PayTypeActivity.class);
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
            holder.detail_price.setText("￥" + df.format(data.get(position).getMoney()));
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
