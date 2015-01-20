package org.atmosphere.vibe.example.platform.grizzly2;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.grizzly2.VibeHttpHandler;
import org.atmosphere.vibe.platform.bridge.grizzly2.VibeWebSocketApplication;
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;

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

        HttpTransportServer httpTransportServer = new HttpTransportServer().transportAction(server);
        WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().transportAction(server);

        HttpServer httpServer = HttpServer.createSimpleServer();
        ServerConfiguration config = httpServer.getServerConfiguration();
        config.addHttpHandler(new VibeHttpHandler().httpAction(httpTransportServer), "/vibe");
        NetworkListener listener = httpServer.getListener("grizzly");
        listener.registerAddOn(new WebSocketAddOn());
        WebSocketEngine.getEngine().register("", "/vibe", new VibeWebSocketApplication().wsAction(wsTransportServer));
        httpServer.start();
        System.in.read();
    }
}