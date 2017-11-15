package com.yonggang.liyangyang.businesscard.app_landscape.http;

/**
 * Created by liyangyang on 2017/3/25.
 */

public class HttpResult<T> {
    private String flag;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
