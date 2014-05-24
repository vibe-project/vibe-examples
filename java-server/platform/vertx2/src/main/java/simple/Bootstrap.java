package simple;

import org.atmosphere.vibe.Action;
import org.atmosphere.vibe.runtime.DefaultServer;
import org.atmosphere.vibe.runtime.Server;
import org.atmosphere.vibe.runtime.Socket;
import org.atmosphere.vibe.vertx.VertxBridge;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

public class Bootstrap extends Verticle {
    @Override
    public void start() {
        final Server server = new DefaultServer();
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

        HttpServer httpServer = vertx.createHttpServer();
        new VertxBridge(httpServer, "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
        httpServer.listen(8080);
    }
}