package com.yonggang.liyangyang.businesscard.response;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.Entry.Data;

/**
 * Created by liyangyang on 2017/3/28.
 */

public class OrderDetailResult {
    private String flag;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
