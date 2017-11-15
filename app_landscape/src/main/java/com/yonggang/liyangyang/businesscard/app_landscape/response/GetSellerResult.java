package com.yonggang.liyangyang.businesscard.app_landscape.response;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Seller;

/**
 * Created by liyangyang on 2017/3/24.
 */

public class GetSellerResult {
    private String flag;
    private String msg;
    private Seller seller;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
