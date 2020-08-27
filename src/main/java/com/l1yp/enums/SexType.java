package com.l1yp.enums;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
public enum SexType implements BaseEnum {
    ALL(0),
    WOMAN(2),
    MAN(1),
    ;

    int code;

    SexType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
