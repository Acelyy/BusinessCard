package com.yonggang.liyangyang.businesscard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.liyangyang.businesscard.Entry.Data;
import com.yonggang.liyangyang.businesscard.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.request.GetSellerList;
import com.yonggang.liyangyang.businesscard.response.GetSellerOrderResponse;
import com.yonggang.liyangyang.businesscard.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class QueryOrderActivity extends BaseActivity {

    List<Data> list_data = new ArrayList<Data>();

    int total;
    @BindView(R.id.lv_order)
    PullToRefreshListView lvOrder;

    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.money_sum)
    TextView moneySum;

    private String start_time;
    private String end_time;

    private AlertDialog dialog;

    private OrderAdapter adapter;

    SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_order);
        ButterKnife.bind(this);
        start_time = format.format(new Date());
        end_time = format.format(new Date());
        startTime.setText(start_time);
        endTime.setText(end_time);
        getSellerList();
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("trade_id", list_data.get(position - 1).getTrade_id());
                stepActivity(bundle, OrderDetailActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        getSellerList();
        super.onResume();
    }

    private void getSellerList() {
        GetSellerList getSellerList = HttpUtil.getService(GetSellerList.class);
        getSellerList.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), start_time, end_time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSellerOrderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetSellerOrderResponse gst) {
                        Log.i("s", gst.toString());
                        String flag = gst.getFlag();
                        if ("100".equals(flag)) {
                            list_data = gst.getData();
                            adapter = new OrderAdapter();
                            lvOrder.setAdapter(adapter);
                            moneySum.setText(sumPrice() + "");
                        } else {
                            Toast.makeText(QueryOrderActivity.this, gst.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 计算总价
     */
    private double sumPrice() {
        double sum = 0;
        for (Data data : list_data) {
            sum += Double.parseDouble(data.getTrade_money());
        }
        return sum;
    }

    @OnClick({R.id.start_time, R.id.end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time:
                LayoutInflater inflater = getLayoutInflater();
                View customView = inflater.inflate(R.layout.custom_date_picker, null);

                // Define your date pickers
                final DatePicker dpStartDate = (DatePicker) customView.findViewById(R.id.dpDate);

                // Build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(customView); // Set the view of the dialog to your custom layout
                builder.setTitle("选择开始日期");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start_time = format.format(new Date(dpStartDate.getYear() - 1900, dpStartDate.getMonth(), dpStartDate.getDayOfMonth()));
                        startTime.setText(start_time);
                        getSellerList();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.end_time:
                LayoutInflater inflater2 = getLayoutInflater();
                View customView2 = inflater2.inflate(R.layout.custom_date_picker, null);

                // Define your date pickers
                final DatePicker dpEndDate = (DatePicker) customView2.findViewById(R.id.dpDate);

                // Build the dialog
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(customView2); // Set the view of the dialog to your custom layout
                builder2.setTitle("选择结束日期");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        end_time = format.format(new Date(dpEndDate.getYear() - 1900, dpEndDate.getMonth(), dpEndDate.getDayOfMonth()));
                        endTime.setText(end_time);
                        getSellerList();
                    }
                });
                dialog = builder2.create();
                dialog.show();
                break;
        }
    }

    class OrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return list_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_order, null);
                holder.bill_number = (TextView) convertView.findViewById(R.id.bill_number);
                holder.creater = (TextView) convertView.findViewById(R.id.creater);
                holder.card_num = (TextView) convertView.findViewById(R.id.card_num);
                holder.createtime = (TextView) convertView.findViewById(R.id.createtime);
                holder.trade_money = (TextView) convertView.findViewById(R.id.trade_money);
                holder.discount_money = (TextView) convertView.findViewById(R.id.discount_money);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bill_number.setText(list_data.get(position).getBill_number());
            holder.creater.setText(list_data.get(position).getCreater());
            holder.card_num.setText(list_data.get(position).getCard_num());
            holder.createtime.setText(list_data.get(position).getTrade_time());
            holder.trade_money.setText("￥" + list_data.get(position).getTrade_money());
            holder.discount_money.setText("￥" + list_data.get(position).getDiscount_money());
            return convertView;
        }

        class ViewHolder {
            TextView bill_number;
            TextView creater;
            TextView card_num;
            TextView createtime;
            TextView trade_money;
            TextView discount_money;
        }

    }
}


