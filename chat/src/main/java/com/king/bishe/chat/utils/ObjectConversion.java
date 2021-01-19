package com.king.bishe.chat.utils;

import com.alibaba.fastjson.JSONObject;
import com.king.bishe.chat.Pojo.Chat;
import org.springframework.stereotype.Component;

/** 对象转换
 * @author king
 * @date 2020/11/12 14:27
 */
@Component
public class ObjectConversion {

    public JSONObject StringToJSON(String str) {
        return JSONObject.parseObject(str);
    }

    public Chat JSONToChat(JSONObject jsonObject) {
        Chat chat = new Chat();
        chat.setId(0);
        chat.setFromWho(jsonObject.getString("from"));
        chat.setTarget(jsonObject.getString("target"));
        chat.setMsg(jsonObject.getString("msg"));
        chat.setMsgType(jsonObject.getString("type"));
        return chat;
    }
}
