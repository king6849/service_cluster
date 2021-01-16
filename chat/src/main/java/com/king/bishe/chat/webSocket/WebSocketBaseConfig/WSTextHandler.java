package com.king.bishe.chat.webSocket.WebSocketBaseConfig;


import com.king.bishe.chat.webSocket.WebSocketTemplate.AbstractWebSocketService;
import com.king.bishe.chat.webSocket.WebSocketTemplate.WsUserListImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WSTextHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final WsUserListImpl wsUserList = new WsUserListImpl();

    private final ChannelGroup userChannelGroup = AbstractWebSocketService.getChannelGroup();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        msg = msg.retain();
        try {
            wsUserList.dispatch(ctx, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        ChannelId id = ctx.channel().id();
        userChannelGroup.add(ctx.channel());
        log.info("新连接加入,id is {}", id.asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)  {
        ChannelId id = ctx.channel().id();
        userChannelGroup.remove(ctx.channel());
        log.info("断开连接,id is {}", id.asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
