package com.yonggang.liyangyang.businesscard.response;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.Entry.Data;

import java.util.List;

/**
 * Created by liyangyang on 2017/3/27.
 */

public class GetSellerOrderResponse {
    private String flag;
    private String msg;
    private int total;
    private List<Data> data;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
