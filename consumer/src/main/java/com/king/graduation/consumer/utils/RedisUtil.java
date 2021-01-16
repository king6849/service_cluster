package com.king.graduation.consumer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author king
 * @date 2020/11/20
 */
@Component
public class RedisUtil {
    private static final long expireTime = 60 * 3;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void stringSet(String key, String value) {
        redisTemplate.opsForValue().set(key, value, expireTime);
    }

    public String stringGet(String key) {
        Object str = redisTemplate.opsForValue().get(key);
        if (str == null) return null;
        return Objects.requireNonNull(redisTemplate.opsForValue().get(key));
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

}
