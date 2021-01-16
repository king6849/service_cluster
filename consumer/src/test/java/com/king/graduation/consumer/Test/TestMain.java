package com.king.graduation.consumer.Test;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.UUID;

/**
 * @Author king
 * @date 2020/11/30
 */
public class TestMain {
    
    @Test
    public void UUIDTest(){
        String uuid= UUID.randomUUID().toString();
        System.out.println(uuid);
        String noncestr = RandomStringUtils.randomAlphanumeric(16);
        System.out.println(noncestr);
    }
    
}
