package com.yonggang.liyangyang.businesscard.Entry;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class Card implements Serializable {
    /**
     * 当前余额=recharge_money+donation_money+transfer_money-consume_money
     */
    private String id;
    private String name;
    private String card_num;//卡号
    private String card_sn;
    private String card_type;
    private String card_pwd;//支付密码
    private double consume_money;//消费总额
    private String deposit;
    private double donation_money;//赠送余额
    private String headimgurl;
    private String openid;
    private String pay_code;
    private String phone;
    private double recharge_money;//充值总额
    private String smsg_push;
    private String status;
    private String weixin_push;
    private String card_serialnumber;
    private double discount;//折扣
    private double transfer_money;

    public String getCard_pwd() {
        return card_pwd;
    }

    public void setCard_pwd(String card_pwd) {
        this.card_pwd = card_pwd;
    }

    public double getTransfer_money() {
        return transfer_money;
    }

    public void setTransfer_money(double transfer_money) {
        this.transfer_money = transfer_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_sn() {
        return card_sn;
    }

    public void setCard_sn(String card_sn) {
        this.card_sn = card_sn;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public double getConsume_money() {
        return consume_money;
    }

    public void setConsume_money(double consume_money) {
        this.consume_money = consume_money;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public double getDonation_money() {
        return donation_money;
    }

    public void setDonation_money(double donation_money) {
        this.donation_money = donation_money;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(double recharge_money) {
        this.recharge_money = recharge_money;
    }

    public String getSmsg_push() {
        return smsg_push;
    }

    public void setSmsg_push(String smsg_push) {
        this.smsg_push = smsg_push;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeixin_push() {
        return weixin_push;
    }

    public void setWeixin_push(String weixin_push) {
        this.weixin_push = weixin_push;
    }

    public String getCard_serialnumber() {
        return card_serialnumber;
    }

    public void setCard_serialnumber(String card_serialnumber) {
        this.card_serialnumber = card_serialnumber;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
