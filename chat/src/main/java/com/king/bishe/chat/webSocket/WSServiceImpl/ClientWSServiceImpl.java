package com.king.bishe.chat.webSocket.WSServiceImpl;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author king
 * @date 2020/11/12 14:21
 */
@Slf4j
@Component(value = "clientWSService")
public class ClientWSServiceImpl extends StaffWSServiceImpl {
    @Override
    public JSONObject history(String receiver) {
        for (String staffName : staffChannel.keySet()) {
            Channel targetChannel = customerChannel.get(receiver);
            log.info("客户{}拉取客服：{}的缓存消息", receiver, staffName);
            super.doPullHistory(staffName, targetChannel);
        }
        return null;
    }

}
