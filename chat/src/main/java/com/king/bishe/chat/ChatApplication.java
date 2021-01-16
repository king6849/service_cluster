package com.king.bishe.chat;

import com.king.bishe.chat.webSocket.WebSocketBaseConfig.WebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("com.king.bishe.chat.mapper")
@EnableEurekaClient
@EnableFeignClients
public class ChatApplication {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //开启websocket
    public static void startWebsocketService() {
        new Thread(new WebSocketServer()).start();
    }

    private void initRedisTemplate() {
        RedisSerializer serializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
    }

    @PostConstruct
    public void init() {
        initRedisTemplate();
    }

    public static void main(String[] args) {
        startWebsocketService();
        SpringApplication.run(ChatApplication.class, args);
    }

}
