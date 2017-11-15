package com.yonggang.liyangyang.businesscard.app_landscape.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Goods;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liyangyang on 2017/3/18.
 */

public class Util {
    //public static final String SHARED_GOODS = "shared_goods";
    public static final String SHARED_DISCOUNT = "shared_discount";
    public static final String SHARED_DEVICE = "shared_device";
    public static final String SHARED_USER = "shared_user";


    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceNo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_DEVICE, MODE_PRIVATE);
        String device_no = sp.getString("device_no", "");
        return device_no;
    }

    /**
     * 保存设备号
     *
     * @param device_no
     * @param context
     */
    public static void setDeviceNo(String device_no, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_DEVICE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("device_no", device_no);
        editor.commit();
    }

    /**
     * 获取激活码
     *
     * @param context
     * @return
     */
    public static String getAuthCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_DEVICE, MODE_PRIVATE);
        String auto_code = sp.getString("auth_code", "");
        return auto_code;
    }

    /**
     * 保存激活码
     *
     * @param auth_code
     * @param context
     */
    public static void setAuthCode(String auth_code, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_DEVICE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("auth_code", auth_code);
        editor.commit();
    }

    /**
     * 保存账号密码
     *
     * @param userName
     * @param password
     * @param context
     */
    public static void setSharedUser(String userName, String password, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_USER, MODE_PRIVATE);
        String userName = sp.getString("userName", "");
        return userName;
    }

    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public static String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_USER, MODE_PRIVATE);
        String password = sp.getString("password", "");
        return password;
    }

    public static void clearGoodsNum(List<Goods> goods) {
        for (Goods g : goods) {
            g.setAmount(0);
        }
    }

}
