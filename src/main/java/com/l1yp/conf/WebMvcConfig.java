package com.l1yp.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.l1yp.enums.EnumBase;
import com.l1yp.enums.SexType;
import com.l1yp.model.UserInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(EnumBase.class, new EnumBaseSerializer());
        module.addDeserializer(Enum.class, new EnumBaseDeserializer());
        mapper.registerModule(module);

        String json = "{\"id\": 1, \"name\": \"Lyp\", \"sex\": 10, \"type\": 0}";
        UserInfo userInfo = mapper.readValue(json, UserInfo.class);
        System.out.println("userInfo = " + userInfo);
        json = mapper.writeValueAsString(userInfo);
        System.out.println(json);
        userInfo = mapper.readValue(json, UserInfo.class);
        System.out.println("userInfo = " + userInfo);
    }

}
