package com.king.bishe.chat.controller;

import com.king.bishe.chat.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author king
 * @date 2020/11/13 19:14
 */
@RestController
public class testController {

    @Resource
    private ClientMapper clientMapper;

    @GetMapping("/test")
    public int test() {
        System.out.println(clientMapper.getChat(1));
        return 1;
    }
}
