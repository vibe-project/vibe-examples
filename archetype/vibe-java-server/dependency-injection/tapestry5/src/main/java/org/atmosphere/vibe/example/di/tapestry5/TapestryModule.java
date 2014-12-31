package org.atmosphere.vibe.example.di.tapestry5;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBuilder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;

public class TapestryModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(Clock.class);
        binder.bind(Server.class, new ServiceBuilder<Server>() {
            @Override
            public Server buildService(ServiceResources resources) {
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
                return server;
            }
        });
    }
}
