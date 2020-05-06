package com.yonggang.liyangyang.businesscard;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.Entry.Goods;
import com.yonggang.liyangyang.businesscard.util.Dao;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGoodsActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_price)
    EditText editPrice;
    @BindView(R.id.edit_wt)
    EditText edit_wt;

    Dao dao;

    DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        df.setRoundingMode(RoundingMode.DOWN);
        ButterKnife.bind(this);
        dao = new Dao(this);
    }

    @OnClick({R.id.btn_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
                addGoods(editName.getText().toString(), editPrice.getText().toString(), edit_wt.getText().toString());
                break;
        }
    }

    /**
     * 添加商品
     *
     * @param name
     * @param price
     * @param wt
     */
    private void addGoods(String name, String price, String wt) {
        if ("".equals(name)) {
            Toast.makeText(this, "商品名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(price)) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(wt)) {
            Toast.makeText(this, "编号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        double d1 = Double.parseDouble(price);
        String s1 = df.format(d1);
        double double_price = Double.parseDouble(s1);
        if (double_price == 0) {
            Toast.makeText(this, "价格不能为0", Toast.LENGTH_SHORT).show();
            return;
        }
        Goods goods = new Goods();
        goods.setGood_name(name);
        goods.setPrice(double_price);
        goods.setGoods_wt(Integer.parseInt(wt));
        dao.saveGoods(goods);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        editName.setText("");
        editPrice.setText("");
        edit_wt.setText("");
    }
}
