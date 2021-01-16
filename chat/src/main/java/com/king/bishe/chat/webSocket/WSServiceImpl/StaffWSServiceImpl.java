package com.king.bishe.chat.webSocket.WSServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.bishe.chat.Pojo.User;
import com.king.bishe.chat.utils.RedisUtil;
import com.king.bishe.chat.webSocket.WebSocketTemplate.AbstractWebSocketService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author king
 * @date 2020/11/12 14:21
 */
@Slf4j
@Component(value = "staffWSService")
public class StaffWSServiceImpl implements WSUserService {
    protected static HashMap<String, User> onLineUser = new HashMap<>(1024);
    protected final ConcurrentHashMap<String, Channel> staffChannel = AbstractWebSocketService.getStaffChannel();
    protected final ConcurrentHashMap<String, Channel> customerChannel = AbstractWebSocketService.getCustomerChannel();

    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Autowired
    protected RedisUtil redisUtil;

    @Override
    public JSONObject history(String receiver) {
        for (String staffName : staffChannel.keySet()) {
            Channel channel = staffChannel.get(staffName);
            for (String user : customerChannel.keySet()) {
                log.info("客服：{}拉取用户：{}的缓存消息", staffName, user);
                doPullHistory(user, channel);
            }
        }
        return null;
    }

    /**
     * @Description 拉取target的缓存信息给targetChannel
     *              这里是 客服拉取指定客户 target 的消息给自己
     * @Author king
     * @Date 2020/11/13 15:08
     */
    protected void doPullHistory(String targetUser, Channel targetStaffChannel) {
        List<String> listValue = redisUtil.getListValue(targetUser);
        if (listValue == null || listValue.size() == 0) return;
        log.info("缓存的消息{}", Arrays.toString(listValue.toArray()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "unread");
        jsonObject.put("user", targetUser);
        jsonObject.put("createTime", formatter.format(new Date()));
        jsonObject.put("msgList", listValue);
        //返回结果应该是 {type:history,name:['你好','在吗']}
        targetStaffChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
    }

    @Async
    @Override
    public void haveRead(String nickName) {
        redisUtil.haveRead(nickName);
        log.info("{} 已读取缓存", nickName);
    }


    @Override
    public JSONObject allOnLineUser() {
        return null;
    }

    @Override
    public User onLineUser(String nickName) {
        return onLineUser.get(nickName);
    }

    public static HashMap<String, User> getOnLineUser() {
        return onLineUser;
    }
}
