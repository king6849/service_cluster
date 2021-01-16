package com.king.bishe.chat.webSocket.WSServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.bishe.chat.Pojo.User;

public interface WSUserService {
    JSONObject history(String receiver);

    void haveRead(String nickName);

    JSONObject allOnLineUser();

    User onLineUser(String nickName);
}
