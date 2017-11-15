package com.yonggang.liyangyang.businesscard.app_landscape.response;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Card;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class GetCardByCodeResult {
    private String flag;
    private Card card;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
