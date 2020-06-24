package com.l1yp.conf;

import com.l1yp.conf.RedisKey.Builder;

import java.time.Duration;

/**
 * @Author Lyp
 * @Date   2020-06-23
 * @Email  l1yp@qq.com
 */
public interface RedisKeyConst {

    /***
     * 将key和过期时间统一管理,
     */
    Builder VERIFY_CODE = new Builder("verify_code", Duration.ofMinutes(10));
    Builder USER_INFO = new Builder("user_info",       Duration.ofDays(30));
    Builder CUSTOM_INFO = new Builder("custom_info",    Duration.ofDays(30));

}
