package com.l1yp.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        String json = "{\"id\": 1, \"name\": \"Lyp\", \"sex\": 1}";
        UserInfo userInfo = mapper.readValue(json, UserInfo.class);
        System.out.println("userInfo = " + userInfo);

        System.out.println(mapper.writeValueAsString(userInfo));

    }

}
