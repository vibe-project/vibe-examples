package org.atmosphere.vibe.example.platform.vertx2;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.vertx2.VibeRequestHandler;
import org.atmosphere.vibe.platform.bridge.vertx2.VibeWebSocketHandler;
import org.atmosphere.vibe.platform.http.ServerHttpExchange;
import org.atmosphere.vibe.platform.ws.ServerWebSocket;
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

public class Bootstrap extends Verticle {
    @Override
    public void start() {
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

        HttpServer httpServer = vertx.createHttpServer();
        RouteMatcher httpMatcher = new RouteMatcher();
        httpMatcher.all("/vibe", new VibeRequestHandler() {
            @Override
            protected Action<ServerHttpExchange> httpAction() {
                return httpTransportServer;
            }
        });
        httpServer.requestHandler(httpMatcher);
        final VibeWebSocketHandler websocketHandler = new VibeWebSocketHandler() {
            @Override
            protected Action<ServerWebSocket> wsAction() {
                return wsTransportServer;
            }
        };
        httpServer.websocketHandler(new Handler<org.vertx.java.core.http.ServerWebSocket>() {
            @Override
            public void handle(org.vertx.java.core.http.ServerWebSocket socket) {
                if (socket.path().equals("/vibe")) {
                    websocketHandler.handle(socket);
                }
            }
        });
        httpServer.listen(8080);
    }
}