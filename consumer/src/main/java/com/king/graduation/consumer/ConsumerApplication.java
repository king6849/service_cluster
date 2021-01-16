package com.king.graduation.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ConsumerApplication {

    @Autowired
    private RedisTemplate redisTemplate;

    private void initRedisTemplate() {
        RedisSerializer serializer = redisTemplate.getStringSerializer();
        redisTemplate.setStringSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
    }

    @PostConstruct
    public void init() {
        initRedisTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
