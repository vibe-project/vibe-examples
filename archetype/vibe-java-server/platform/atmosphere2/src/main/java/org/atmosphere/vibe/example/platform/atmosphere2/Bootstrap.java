package org.atmosphere.vibe.example.platform.atmosphere2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.atmosphere2.VibeAtmosphereServlet;
import org.atmosphere.vibe.platform.http.ServerHttpExchange;
import org.atmosphere.vibe.platform.ws.ServerWebSocket;
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;

@WebListener
public class Bootstrap implements ServletContextListener {
    @SuppressWarnings("serial")
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final Server server = new DefaultServer();
        server.socketAction(new Action<ServerSocket>() {
            @Override
            public void on(final ServerSocket socket) {
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
        
        final HttpTransportServer httpTransportServer = new HttpTransportServer().transportAction(server);
        final WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().transportAction(server);

        ServletContext context = event.getServletContext();
        ServletRegistration.Dynamic reg = context.addServlet(VibeAtmosphereServlet.class.getName(), new VibeAtmosphereServlet() {
            @Override
            protected Action<ServerHttpExchange> httpAction() {
                return httpTransportServer;
            }

            @Override
            protected Action<ServerWebSocket> wsAction() {
                return wsTransportServer;
            }
        });
        reg.setAsyncSupported(true);
        reg.setInitParameter(ApplicationConfig.DISABLE_ATMOSPHEREINTERCEPTOR, Boolean.TRUE.toString());
        reg.addMapping("/vibe");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
