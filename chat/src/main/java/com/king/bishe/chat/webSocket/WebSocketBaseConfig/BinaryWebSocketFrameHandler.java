package com.king.bishe.chat.webSocket.WebSocketBaseConfig;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 传输二进制文件格式
 *
 * @author king
 */
public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) {
//        saveConnectionChannel(ctx, msg);
        ByteBuf content = msg.content();
        content.writeBytes(msg.content());

    }


}