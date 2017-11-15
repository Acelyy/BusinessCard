package com.yonggang.liyangyang.businesscard.response;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.Entry.User;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class ActivateResult {
    private String flag;
    private User user;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
