package com.king.bishe.chat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author king
 * @date 2020/11/9 23:40
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<String> getListValue(String key) {
        ListOperations<String, String> list = redisTemplate.opsForList();
        return redisTemplate.opsForList().range(key, 0, list.size(key));
    }

    public void saveMsg(String username, String msg) {
        redisTemplate.opsForList().rightPush(username, msg);
    }

    public void haveRead(String key) {
        redisTemplate.delete(key);
    }

}
