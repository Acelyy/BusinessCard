package com.yonggang.liyangyang.businesscard.app_landscape.Entry;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/3/24.
 */

public class Seller implements Serializable{
    private String phone;
    private String discount;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
