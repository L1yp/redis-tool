package com.l1yp.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@JsonInclude()
public interface BaseEnum {

    int getCode();

    static <V extends Enum> Enum ofCode(Class<V> clazz, int code){
        if (BaseEnum.class.isAssignableFrom(clazz)){
            V[] enumConstants = clazz.getEnumConstants();
            for (V enumConstant : enumConstants) {
                if (((BaseEnum)enumConstant).getCode() == code) {
                    return enumConstant;
                }
            }
        }

        return null;
    }


}
