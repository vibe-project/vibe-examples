package simple;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.servlet3.ServletBridge;
import org.atmosphere.vibe.server.DefaultServer;
import org.atmosphere.vibe.server.Server;
import org.atmosphere.vibe.server.ServerSocket;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
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
        
        new ServletBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
