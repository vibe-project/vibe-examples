package org.atmosphere.vibe.example.di.dagger1;

import javax.inject.Singleton;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;

import dagger.Module;
import dagger.Provides;

@Module(injects = Bootstrap.class)
public class DaggerModule {
    // Registers the server as a component
    @Provides
    @Singleton
    public Server server() {
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
}
