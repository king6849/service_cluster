package com.king.bishe.chat.WSTransform;

import com.alibaba.fastjson.JSON;
import com.king.bishe.chat.Pojo.User;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;
/** 二进制化 传输对象 ,解码
 * @author king
 */
public class UserDecoder implements javax.websocket.Decoder.Text<User> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public User decode(String user) throws DecodeException {
        return JSON.parseObject(user, User.class);
    }

    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

}