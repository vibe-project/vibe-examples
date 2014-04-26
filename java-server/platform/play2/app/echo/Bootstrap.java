package echo;

import io.react.Action;
import io.react.play.PlayServerHttpExchange;
import io.react.play.PlayServerWebSocket;
import io.react.runtime.DefaultServer;
import io.react.runtime.Server;
import io.react.runtime.Socket;

import play.api.mvc.Codec;
import play.core.j.JavaResults;
import play.libs.F.Callback0;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Results.Chunks;
import play.mvc.WebSocket;

public class Bootstrap extends Controller {
    static Server server = new DefaultServer();
    static {
        server.socketAction(new Action<Socket>() {
            @Override
            public void on(final Socket socket) {
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
                server.websocketAction().on(new PlayServerWebSocket(request, in, out));
            }
        };
    }
}
