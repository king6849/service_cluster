package com.king.bishe.chat.webSocket.WebSocketBaseConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServer implements Runnable{

    private static final int port = 8888;

    private static final String webSocketUrl = "/ws";

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private static EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    private static ServerBootstrap bootstrap = new ServerBootstrap();

    //编写run方法，处理客户端的请求
    public void startWebSocketServer() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-decoder", new HttpServerCodec());
                            // 加入ObjectAggregator解码器，作用是他会把多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                            // 加入chunked 主要作用是支持异步发送的码流（大文件传输），但不专用过多的内存，防止java内存溢出
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 加入webSocket的hanlder
                            pipeline.addLast(new WebSocketServerProtocolHandler(webSocketUrl));
                            // 自定义处理器 - 处理 web socket 二进制消息
                            pipeline.addLast(new BinaryWebSocketFrameHandler());
                            //加入自己的业务处理handler
                            pipeline.addLast(new WSTextHandler());
                        }
                    });
            log.info("netty服务启动");
            try {
                ChannelFuture channelFuture = bootstrap.bind(port).sync();
                //监听关闭
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {
        this.startWebSocketServer();
    }
}
