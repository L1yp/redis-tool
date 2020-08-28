package com.l1yp.conf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.l1yp.enums.EnumBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lyp
 * @Date   2020-08-27
 * @Email  l1yp@qq.com
 */
public class EnumBaseSerializer extends JsonSerializer<EnumBase> {

    @Override
    public void serialize(EnumBase value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getCode());
    }
}
