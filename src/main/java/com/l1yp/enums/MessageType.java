package com.l1yp.enums;

/**
 * @Author Lyp
 * @Date 2020-08-28
 * @Email l1yp@qq.com
 */
public enum  MessageType {
    text(100, "text"),
    image(200, "image"),
    ;

    private int code;
    private String message;

    MessageType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
