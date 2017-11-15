package cn.innovate.rfidservice.entity;

import java.util.Date;

/**
 * Created by sofox on 16-7-13.
 */
public class LogItem {
    private Date when;
    private String message;

    public LogItem(String message) {
        this.message = message;
        this.when = new Date();
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
