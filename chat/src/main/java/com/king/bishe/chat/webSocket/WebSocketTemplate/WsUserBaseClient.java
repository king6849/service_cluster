package com.king.bishe.chat.webSocket.WebSocketTemplate;

import com.alibaba.fastjson.JSONObject;

import com.king.bishe.chat.Pojo.Chat;
import com.king.bishe.chat.Pojo.Role;
import com.king.bishe.chat.mapper.ClientMapper;
import com.king.bishe.chat.utils.ObjectConversion;
import com.king.bishe.chat.utils.RedisUtil;
import com.king.bishe.chat.utils.SpringBeanUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

@Slf4j
public class WsUserBaseClient extends AbstractWebSocketService {

    protected RedisUtil redisUtil = SpringBeanUtil.getBean(RedisUtil.class);
    protected ClientMapper clientMapper = SpringBeanUtil.getBean(ClientMapper.class);
    protected ObjectConversion objectConversion = SpringBeanUtil.getBean(ObjectConversion.class);

    /*userInfo: {
        avatar: 'https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar',
                nickName: 'name1',
                role: 0
    }*/
    //按用户类型分类
    @Override
    public void sort(int role, String id, Channel channel) {
        if (role == Role.StaffLevel.getRoleLevel()) {
            staffChannel.put(id, channel);
        } else if (role == Role.PojoUserLevel.getRoleLevel()) {
            customerChannel.put(id, channel);
        }
    }

    //用户离线
    public void offline(String id) {
        if (id != null) {
            userChannel.remove(id);
            customerChannel.remove(id);
            staffChannel.remove(id);
        }
    }

    /**
     * @Description 将消息从缓存中移除，持久化到数据库
     * @Author king
     * @Date 2020/11/13 16:11
     */
    @Async
    protected void saveMessage(JSONObject msgObj) {
        Chat chat = objectConversion.JSONToChat(msgObj);
        chat.setMakeTime(new Date());
        clientMapper.saveMessage(chat);
        log.info("持久化缓存消息：{}", chat.toString());
    }

    // 一对一发送消息
    @Override
    public void chat(ChannelHandlerContext ctx, String target, TextWebSocketFrame data) {
        //消息格式：chatMsg: {type: "chat", from: '', target: '', msg: ""},
        try {
            super.chat(ctx, target, data);
        } catch (Exception e) {
            log.info("用户{}已离线", target);
            offline(target);
            //缓存格式  from_name:['','','']
            JSONObject jsonObject = objectConversion.StringToJSON(data.text());
            redisUtil.saveMsg(jsonObject.getString("from"), jsonObject.getString("msg"));
            //持久化
            saveMessage(jsonObject);
            log.info("已给用户:{}缓存消息:{}", jsonObject.getString("from"), jsonObject.getString("msg"));
        }
    }

}
