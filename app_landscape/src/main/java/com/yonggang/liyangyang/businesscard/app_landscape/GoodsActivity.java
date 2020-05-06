package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Goods;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.GetSeller;
import com.yonggang.liyangyang.businesscard.app_landscape.response.GetSellerResult;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Dao;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoodsActivity extends BaseActivity {

    @BindView(R.id.grid_goods)
    GridView gridGoods;
    @BindView(R.id.btn_pay)
    Button btn_pay;

    List<Goods> data = new ArrayList<Goods>();
    GridAdapter adapter;

    Dao dao;

    double sum;

    ExistBroadcast existBroadcast;

    DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        setTitle("永联旅游一卡通——收银系统");
        ButterKnife.bind(this);
        df.setRoundingMode(RoundingMode.DOWN);
        existBroadcast = new ExistBroadcast();
        IntentFilter filter = new IntentFilter("exit");
        registerReceiver(existBroadcast, filter);

        dao = new Dao(this);
        adapter = new GridAdapter();
        gridGoods.setAdapter(adapter);
        gridGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.get(position).setAmount(data.get(position).getAmount() + 1);
                adapter.notifyDataSetChanged();
            }
        });
        gridGoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                builder.setTitle("删否删除此商品？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.deleteGoods(data.get(position));
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();

                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        data = dao.readGoods();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(existBroadcast);
        super.onDestroy();
    }

    @OnClick(R.id.btn_pay)
    public void onClick() {
        List<Goods> goods = new ArrayList<Goods>();
        for (Goods g : data) {
            if (g.getAmount() > 0) {
                goods.add(g);
            }
        }
        if (goods.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_input, null);
            final EditText editText = (EditText) view.findViewById(R.id.input);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请输入消费金额")
                    .setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("goods_list", new ArrayList<Goods>());
                            String msg = editText.getText().toString();
                            if (!"".equals(msg)) {
                                bundle.putDouble("sum", Double.parseDouble(df.format(Double.parseDouble(msg))));
                                stepActivity(bundle, OrderActivity.class);
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("goods_list", (Serializable) goods);
            bundle.putDouble("sum", sum);
            stepActivity(bundle, OrderActivity.class);
        }
    }

    /**
     * 计算总价
     */
    private void sumPrice() {
        sum = 0.00;
        for (Goods goods : data) {
            sum += goods.getAmount() * goods.getPrice();
        }
        btn_pay.setText(df.format(sum) + "元");
    }


    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_grid, null);
                holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
                holder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
                holder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.goods_name.setText(data.get(position).getGood_name());
            holder.goods_price.setText(data.get(position).getPrice() + "元");
            holder.goods_num.setText(data.get(position).getAmount() + "");
            holder.goods_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = data.get(position).getAmount();
                    if (num > 0) {
                        num--;
                        data.get(position).setAmount(num);
                    }
                    notifyDataSetChanged();
                }
            });
            sumPrice();
            return convertView;
        }

        class ViewHolder {
            TextView goods_name;
            TextView goods_price;
            TextView goods_num;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goods_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_goods:
                stepActivity(AddGoodsActivity.class);
                break;
            case R.id.setting:
                getInfo();
                break;
            case R.id.query_order:
                stepActivity(QueryOrderActivity.class);
                break;

            case R.id.change_pass:
                stepActivity(ChangePassActivity.class);
                break;
            case R.id.delete_device:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确认解绑设备？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Util.setDeviceNo("", GoodsActivity.this);
                                Util.setAuthCode("", GoodsActivity.this);
                                Util.setSharedUser("", "", GoodsActivity.this);
                                finish();
                            }
                        }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInfo() {
        GetSeller getSeller = HttpUtil.getService(GetSeller.class);
        getSeller.doPost(Util.getDeviceNo(this), Util.getAuthCode(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSellerResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(GetSellerResult gsr) {
                        Log.i("s", gsr.toString());
                        String flag = gsr.getFlag();
                        if ("100".equals(flag)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("seller", gsr.getSeller());
                            stepActivity(bundle, SettingActivity.class);
                        } else {
                            Toast.makeText(GoodsActivity.this, gsr.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    class ExistBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
