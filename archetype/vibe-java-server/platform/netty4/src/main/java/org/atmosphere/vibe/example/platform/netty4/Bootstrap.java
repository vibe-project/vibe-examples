package org.atmosphere.vibe.example.platform.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.URI;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.netty4.VibeServerCodec;
import org.atmosphere.vibe.platform.http.ServerHttpExchange;
import org.atmosphere.vibe.platform.ws.ServerWebSocket;
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;

public class Bootstrap {
    public static void main(String[] args) throws Exception {
        final Server server = new DefaultServer();
        server.socketAction(new Action<ServerSocket>() {
            @Override
            public void on(final ServerSocket socket) {
                System.out.println("on socket: " + socket.uri());
                socket.on("echo", new Action<Object>() {
                    @Override
                    public void on(Object data) {
                        System.out.println("on echo event: " + data);
                        socket.send("echo", data);
                    }
                });
                socket.on("chat", new Action<Object>() {
                    @Override
                    public void on(Object data) {
                        System.out.println("on chat event: " + data);
                        server.all().send("chat", data);
                    }
                });
            }
        });

        final HttpTransportServer httpTransportServer = new HttpTransportServer().transportAction(server);
        final WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().transportAction(server);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec())
                    .addLast(new VibeServerCodec() {
                        @Override
                        protected boolean accept(HttpRequest req) {
                            return URI.create(req.getUri()).getPath().equals("/vibe");
                        }
                        
                        @Override
                        protected Action<ServerHttpExchange> httpAction() {
                            return httpTransportServer;
                        }
                        
                        @Override
                        public Action<ServerWebSocket> wsAction() {
                            return wsTransportServer;
                        }
                    });
                }
            });
            Channel channel = bootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}