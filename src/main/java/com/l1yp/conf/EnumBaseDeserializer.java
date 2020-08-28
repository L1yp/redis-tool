package com.l1yp.conf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.l1yp.enums.EnumBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
public class EnumBaseDeserializer extends JsonDeserializer<Enum> implements ContextualDeserializer {

    private Class<Enum> clazz;
    private Map<Integer, Enum> codeMap;
    private Map<String, Enum> nameMap;
    private Enum[] elems;

    private void setClass(Class<Enum> clazz) {
        this.clazz = clazz;
        this.elems = clazz.getEnumConstants();
        if (EnumBase.class.isAssignableFrom(clazz)){
            codeMap = new HashMap<>();
            for (Enum elem : this.elems) {
                codeMap.put(((EnumBase) elem).getCode(), elem);
            }
        }
    }

    @Override
    public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token.isNumeric()){
            if (EnumBase.class.isAssignableFrom(clazz)){
                int code = p.getIntValue();
                System.out.println("code = " + code);
                return codeMap.get(code);
            }else {
                int original = p.getIntValue();
                return elems[original];
            }
        }else if (token == JsonToken.VALUE_STRING){
            String name = p.getValueAsString();
            return Enum.valueOf(clazz, name);
        }else {
            return null;
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Class<Enum> clazz = (Class<Enum>) ctxt.getContextualType().getRawClass();
        System.out.println("current Type: " + clazz.getName());
        EnumBaseDeserializer desr = new EnumBaseDeserializer();
        desr.setClass(clazz);
        return desr;
    }
}
