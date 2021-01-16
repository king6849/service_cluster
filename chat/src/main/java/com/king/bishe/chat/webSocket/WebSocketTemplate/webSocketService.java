package com.king.bishe.chat.webSocket.WebSocketTemplate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public interface webSocketService {
    //消息分发
    void dispatch(ChannelHandlerContext ctx, Object msg) throws Exception;

    //    用户连接
    void connection(String nickName, ChannelHandlerContext ctx);

    //    客户一对一客服
    void chat(ChannelHandlerContext ctx, String target, TextWebSocketFrame data) throws Exception;

    //    活动推送
    void activityPush();

    //    推送系统公告
    void systemAnnouncement();
}
