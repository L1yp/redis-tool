package com.l1yp.util;

import com.l1yp.conf.RedisKey;
import com.l1yp.conf.RedisTemplateEx;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * @Author Lyp
 * @Date   2020-06-23
 * @Email  l1yp@qq.com
 */

@Component
public class RedisTool {

    @Resource
    private RedisTemplateEx template;

    public boolean set(RedisKey key, Object val) {
        Objects.requireNonNull(val);

        if (key.getExpireAt() == 0 && key.getExpireTime() == null) {
            template.opsForValue().set(key, Objects.requireNonNull(JsonUtils.serialize(val)));
            return true;
        }

        List<Object> txResults = template.execute(new SessionCallback<List<Object>>() {

            @SuppressWarnings("unchecked")
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 开始 redis 事务

                operations.opsForValue().set(key, Objects.requireNonNull(JsonUtils.serialize(val)));

                Duration expireTime = key.getExpireTime();
                long expireAt = key.getExpireAt();
                if (expireTime == null) {
                    operations.expireAt(key, Instant.ofEpochSecond(expireAt));
                } else {
                    operations.expire(key, expireTime);
                }

                return operations.exec(); // 提交redis 事务
            }
        });
        if (txResults != null && txResults.size() == 2) {
            return txResults.get(0) == Boolean.TRUE && txResults.get(1) == Boolean.TRUE;
        }
        return false;
    }

    public boolean set(RedisKey key, String val) {
        Objects.requireNonNull(val);

        if (key.getExpireAt() == 0 && key.getExpireTime() == null) {
            template.opsForValue().set(key, val);
            return true;
        }

        List<Object> txResults = template.execute(new SessionCallback<List<Object>>() {

            @SuppressWarnings("unchecked")
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 开始 redis 事务

                operations.opsForValue().set(key, val);

                Duration expireTime = key.getExpireTime();
                long expireAt = key.getExpireAt();
                if (expireTime == null) {
                    operations.expireAt(key, Instant.ofEpochSecond(expireAt));
                } else {
                    operations.expire(key, expireTime);
                }

                return operations.exec(); // 提交redis 事务
            }
        });
        if (txResults != null && txResults.size() == 2) {
            return txResults.get(0) == Boolean.TRUE && txResults.get(1) == Boolean.TRUE;
        }
        return false;
    }

    public String getString(RedisKey key) {
        return template.opsForValue().get(key);
    }

    public <T> T get(RedisKey key, Class<T> clazz) {
        String val = template.opsForValue().get(key);
        return JsonUtils.deserialize(val, clazz);
    }

    public void mset(Map<RedisKey, String> map) {
        List<Object> txResults = template.execute(new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 开始redis 事务

                operations.opsForValue().multiSet(map);
                for (Entry<RedisKey, String> entry : map.entrySet()) {
                    RedisKey key = entry.getKey();
                    Duration expireTime = key.getExpireTime();
                    long expireAt = key.getExpireAt();
                    if (expireTime == null) {
                        operations.expireAt(key, Instant.ofEpochSecond(expireAt));
                    } else {
                        operations.expire(key, expireTime);
                    }
                }

                return operations.exec();
            }
        });
        System.out.println("txResults = " + txResults);
    }

    public List<String> mget(List<RedisKey> keys) {
        return template.opsForValue().multiGet(keys);
    }

    public boolean delete(RedisKey key) {
        Boolean result = template.delete(key);
        return result == null ? false : result;
    }

}
