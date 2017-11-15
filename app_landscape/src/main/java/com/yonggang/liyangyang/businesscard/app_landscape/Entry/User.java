package com.yonggang.liyangyang.businesscard.app_landscape.Entry;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class User implements Serializable {
    private String userid;
    private String username;
    private String password;
    private String type;
    private String userno;
    private String valid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
