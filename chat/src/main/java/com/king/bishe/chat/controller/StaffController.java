package com.king.bishe.chat.controller;


import com.king.bishe.chat.Pojo.User;
import com.king.bishe.chat.webSocket.WSServiceImpl.StaffWSServiceImpl;
import com.king.bishe.chat.webSocket.WSServiceImpl.WSUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author king
 * @date 2020/11/12 14:30
 */
@RestController
@RequestMapping(path = "/staff")
public class StaffController {
    @Resource
    private WSUserService staffWSService ;

    @RequestMapping(path = "/{nickName}", method = RequestMethod.GET)
    public User onLineUser(@PathVariable String nickName) {
        return staffWSService.onLineUser(nickName);
    }

    @RequestMapping(path = "/unread", method = RequestMethod.GET)
    public int pullUnReadMsg() {
        staffWSService.history("");
        return 200;
    }

    @RequestMapping(path = "/haveread/{nickName}", method = RequestMethod.DELETE)
    public int haveread(@PathVariable String nickName) {
        staffWSService.haveRead(nickName);
        return 200;
    }
}
