package com.king.bishe.chat.controller;


import com.king.bishe.chat.Pojo.User;
import com.king.bishe.chat.webSocket.WSServiceImpl.ClientWSServiceImpl;
import com.king.bishe.chat.webSocket.WSServiceImpl.WSUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Resource
    private WSUserService clientWSService;

    @PostMapping("/test")
    public int test(MultipartFile file, User user) {
        System.out.println(file.getOriginalFilename());
        System.out.println(user.toString());
        return 1;
    }

    @RequestMapping(value = "/unread/{nickName}", method = RequestMethod.GET)
    public int unread(@PathVariable String nickName) {
        clientWSService.history(nickName);
        return 200;
    }

    @RequestMapping(path = "/haveread/{nickName}", method = RequestMethod.DELETE)
    public int haveread(@PathVariable String nickName) {
        clientWSService.haveRead(nickName);
        return 200;
    }
}
