package com.l1yp;

import com.l1yp.model.User;
import com.l1yp.util.RedisTool;
import com.l1yp.conf.RedisKey;
import com.l1yp.conf.RedisKeyConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lyp
 * @Date   2020-06-23
 * @Email  l1yp@qq.com
 */
@SpringBootApplication
public class RedisToolApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RedisToolApplication.class, args);
        RedisTool redisTool = ctx.getBean("redisTool", RedisTool.class);

        RedisKey verifyKey = RedisKeyConst.VERIFY_CODE.build().arg(5).arg("5");
        redisTool.set(verifyKey, "5678");
        System.out.println("verify_code: " + redisTool.getString(verifyKey));

        RedisKey customKey = RedisKeyConst.CUSTOM_INFO.build().arg("666");
        Map<String, Object> map = new HashMap<>(16);
        map.put("kkk", 666);
        redisTool.set(customKey, map);

        Map<String, Object> item = redisTool.get(customKey, Map.class);
        System.out.println("item = " + item);

        RedisKey userRedisKey = RedisKeyConst.USER_INFO.build().arg("lyp");
        boolean result = redisTool.set(userRedisKey, new User(23, "lyp"));
        System.out.println("result = " + result);
        User user = redisTool.get(userRedisKey, User.class);
        System.out.println("user = " + user);

    }




}
