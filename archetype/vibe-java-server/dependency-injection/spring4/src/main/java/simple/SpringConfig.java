package simple;

import javax.servlet.ServletContext;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.DefaultServer;
import org.atmosphere.vibe.server.Server;
import org.atmosphere.vibe.server.ServerSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "simple" })
public class SpringConfig {
    @Autowired
    private ServletContext servletContext;

    @Bean
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
        new AtmosphereBridge(servletContext, "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
        return server;
    }
}