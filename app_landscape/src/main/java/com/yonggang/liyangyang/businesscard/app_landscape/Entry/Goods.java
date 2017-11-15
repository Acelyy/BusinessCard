package com.yonggang.liyangyang.businesscard.app_landscape.Entry;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/3/17.
 */

public class Goods implements Serializable {

    private int id;
    private String good_name;//商品名称
    private double price;//商品单价
    private int amount;//商品数量
    private double money;//商品总价
    private int goods_wt;//商品权重，值越小，显示越靠前

    public Goods() {

    }

    public Goods(int id, String good_name, double price, int goods_wt) {
        this.id = id;
        this.good_name = good_name;
        this.price = price;
        this.goods_wt = goods_wt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        setMoney(getAmount() * getPrice());
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getGoods_wt() {
        return goods_wt;
    }

    public void setGoods_wt(int goods_wt) {
        this.goods_wt = goods_wt;
    }
}
