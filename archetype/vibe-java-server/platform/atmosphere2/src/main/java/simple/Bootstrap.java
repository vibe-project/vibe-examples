package simple;

import org.atmosphere.vibe.Action;
import org.atmosphere.vibe.atmosphere.AtmosphereBridge;
import org.atmosphere.vibe.runtime.DefaultServer;
import org.atmosphere.vibe.runtime.Server;
import org.atmosphere.vibe.runtime.Socket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
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
        
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
