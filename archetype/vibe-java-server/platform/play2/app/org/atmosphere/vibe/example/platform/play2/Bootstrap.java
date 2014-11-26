package org.atmosphere.vibe.example.platform.play2;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.play2.PlayServerHttpExchange;
import org.atmosphere.vibe.platform.server.play2.PlayServerWebSocket;
import org.atmosphere.vibe.server.DefaultServer;
import org.atmosphere.vibe.server.Server;
import org.atmosphere.vibe.server.ServerSocket;

import play.libs.F.Promise;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.WebSocket;

public class Bootstrap extends Controller {
    static Server server = new DefaultServer();
    static {
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
    }

    @BodyParser.Of(BodyParser.TolerantText.class)
    public static Promise<Result> http() {
        PlayServerHttpExchange http = new PlayServerHttpExchange(request(), response());
        server.httpAction().on(http);
        return http.result();
    }
    
    public static WebSocket<String> ws() {
        final Request request = request();
        return new WebSocket<String>() {
            @Override
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                server.wsAction().on(new PlayServerWebSocket(request, in, out));
            }
        };
    }
}
