package com.yonggang.liyangyang.businesscard.app_landscape.http;

/**
 * Created by liyangyang on 2017/3/25.
 */

public class ApiException extends RuntimeException {
    private static final int REQUEST_ERROR = 0;

    public ApiException(int code) {
        this(getApiExceptionMessage(code));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {

            default:
                message = "未知错误";
        }
        return message;
    }
}
