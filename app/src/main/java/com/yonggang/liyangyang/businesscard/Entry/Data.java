package com.yonggang.liyangyang.businesscard.Entry;

import java.util.List;

/**
 * Created by liyangyang on 2017/4/7.
 */

public class Data {
    /**
     * bill_number : 1491546186031
     * card_num : 880001
     * card_type : 2
     * creater : test
     * discount : 1.00
     * discount_money : 2.00
     * name : 鎏金哇咔呀酷列
     * trade_id : 4303
     * trade_money : 2.00
     * trade_time : 2017-04-07 14:23:06
     */

    private String bill_number;
    private String card_num;
    private String card_type;
    private String creater;
    private String discount;
    private String discount_money;
    private String name;
    private String trade_id;
    private String trade_money;
    private String trade_time;
    private List<Goods> item;

    public List<Goods> getItem() {
        return item;
    }

    public void setItem(List<Goods> item) {
        this.item = item;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(String discount_money) {
        this.discount_money = discount_money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getTrade_money() {
        return trade_money;
    }

    public void setTrade_money(String trade_money) {
        this.trade_money = trade_money;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }
}
