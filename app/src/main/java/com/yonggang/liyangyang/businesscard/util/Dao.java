package com.yonggang.liyangyang.businesscard.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yonggang.liyangyang.businesscard.Entry.Goods;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2017/3/21.
 */

public class Dao {
    private DBHelper dbHelper;

    public Dao(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    /**
     * 保存单个商品信息
     *
     * @param goods
     */
    public void saveGoods(Goods goods) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into goods(goods_name,goods_price,goods_wt) values (?,?,?)";
        Object[] bindArgs = {goods.getGood_name(), goods.getPrice(), goods.getGoods_wt()};
        database.execSQL(sql, bindArgs);
    }

    /**
     * 读取所有信息
     *
     * @return
     */
    public List<Goods> readGoods() {
        List<Goods> goods = new ArrayList<Goods>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select _id,goods_name,goods_price,goods_wt from goods order by goods_wt";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Goods g = new Goods(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getInt(3));
            DecimalFormat df = new DecimalFormat("#0.0");
            g.setPrice(Double.parseDouble(df.format(g.getPrice())));
            goods.add(g);
        }
        return goods;
    }

    /**
     * 删除商品
     *
     * @param goods
     */
    public void deleteGoods(Goods goods) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("goods", "_id=?", new String[]{goods.getId() + ""});
    }

}
