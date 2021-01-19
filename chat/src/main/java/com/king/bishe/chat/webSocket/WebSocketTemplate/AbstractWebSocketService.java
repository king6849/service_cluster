package com.king.bishe.chat.webSocket.WebSocketTemplate;

import com.alibaba.fastjson.JSONObject;
import com.king.bishe.chat.Pojo.User;
import com.king.bishe.chat.utils.ObjectConversion;
import com.king.bishe.chat.utils.SpringBeanUtil;
import com.king.bishe.chat.webSocket.WSServiceImpl.StaffWSServiceImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractWebSocketService implements webSocketService {

    protected static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    protected static final ConcurrentHashMap<String, Channel> userChannel = new ConcurrentHashMap<>();

    protected static final ConcurrentHashMap<String, Channel> customerChannel = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, Channel> staffChannel = new ConcurrentHashMap<>();

    protected HashMap<String, User> onLineUser = StaffWSServiceImpl.getOnLineUser();

    protected ObjectConversion objectConversion = SpringBeanUtil.getBean(ObjectConversion.class);
    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //用于区分消息类型，这个是客服聊天系统的建立连接时，根据name-channel建立关系
    protected final static String mappingMark = "chat_basic_info";
    //标记，发送的消息
    protected final static String chatMark = "chat";

    @Override
    public void dispatch(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame data = (TextWebSocketFrame) msg;
            data = data.retain();
            JSONObject jsonObject = (JSONObject) JSONObject.parse(data.text());
            String type = jsonObject.getString("type");
            log.info("type is {}", type);
            //建立连接
            if (type.equals(mappingMark)) {
                //建立连接消息格式：let info = {type: "chat_basic_info", info: this.starffInfo}
                log.info("客户连接信息：{}", jsonObject);
                JSONObject info = (JSONObject) jsonObject.get("info");
                String nickName = info.getString("nickName");
                //建立连接
                connection(nickName, ctx);
                //在线客户
                if (!onLineUser.containsKey(nickName)) {
                    onLineUser.put(nickName, new User(nickName, info.getString("avatar")));
                }
                //用户类型分组
                sort(info.getIntValue("role"), nickName, ctx.channel());
                //一对一发送消息
            } else if (type.equals(chatMark)) {
                log.info("接收到的msg对象:{}", jsonObject);
                //消息格式：textMsg: {type: "chat", from: A, target: B, msg: ""}
                chat(ctx, jsonObject.getString("target"), data);
            }
        } else if (msg instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame data = (BinaryWebSocketFrame) msg;
        }
    }


    @Override
    public void connection(String nickname, ChannelHandlerContext ctx) {
        //建立连接消息格式：let info = {type: "chat_basic_info", info: this.userInfo}
        Channel oldChannel = userChannel.get(nickname);
        Channel currChannel = ctx.channel();
        if (userChannel.containsKey(nickname) && oldChannel.id().equals(currChannel.id())) {
            log.info("已存在用户{}", nickname);
            return;
        }
//        userChannel.put(nickname, allChannel.get(ctx.channel().id()));
        userChannel.put(nickname, ctx.channel());
        log.info("已添加用户映射关系：{}", nickname);
    }

    // 一对一发送消息
    @Override
    public void chat(ChannelHandlerContext ctx, String target, TextWebSocketFrame data) throws Exception {
        //消息格式：textMsg: {type: "chat", from: A, target: B , msg: "你好"},
        Channel targetChannel = userChannel.get(target);
        if (channelGroup.find(targetChannel.id()) == null) {
            userChannel.remove(target);
            throw new Exception("该用户已下线");
        }
        targetChannel.writeAndFlush(msgCreateTime(data.text()));
//        targetChannel.writeAndFlush(data);
    }

    protected TextWebSocketFrame msgCreateTime(String msg) {
        JSONObject jsonObject = objectConversion.StringToJSON(msg);
        jsonObject.put("createTime", formatter.format(new Date()));
        return new TextWebSocketFrame(jsonObject.toJSONString());
    }

    @Async
    abstract public void sort(int level, String id, Channel channel);


    @Override
    public void activityPush() {
    }

    @Override
    public void systemAnnouncement() {

    }

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getUserChannel() {
        return userChannel;
    }

    public static ConcurrentHashMap<String, Channel> getCustomerChannel() {
        return customerChannel;
    }

    public static ConcurrentHashMap<String, Channel> getStaffChannel() {
        return staffChannel;
    }

    public HashMap<String, User> getOnLineUser() {
        return onLineUser;
    }

}
