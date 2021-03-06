package org.atmosphere.vibe.example.di.hk2;

import javax.annotation.PostConstruct;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

// Registers the server as a component
@Service
public class ServerFactory implements Factory<Server> {
    final Server server = new DefaultServer();

    @PostConstruct
    public void init() {
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

    @Override
    public Server provide() {
        return server;
    }

    @Override
    public void dispose(Server instance) {}
}
