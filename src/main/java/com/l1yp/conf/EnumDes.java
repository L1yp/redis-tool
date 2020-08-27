package com.l1yp.conf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.l1yp.enums.BaseEnum;

import java.io.IOException;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
public class EnumDes<E extends Enum<E> & BaseEnum> extends JsonDeserializer<E> {


    @Override
    public E deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
