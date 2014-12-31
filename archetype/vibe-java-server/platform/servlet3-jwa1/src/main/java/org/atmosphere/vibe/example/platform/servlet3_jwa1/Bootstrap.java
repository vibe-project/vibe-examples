package org.atmosphere.vibe.example.platform.servlet3_jwa1;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.atmosphere.vibe.DefaultServer;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.ServerSocket;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.jwa1.VibeServerEndpoint;
import org.atmosphere.vibe.platform.bridge.servlet3.VibeServlet;
import org.atmosphere.vibe.platform.http.ServerHttpExchange;
import org.atmosphere.vibe.platform.ws.ServerWebSocket;

@WebListener
public class Bootstrap implements ServletContextListener {
    @SuppressWarnings("serial")
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
        // Servlet
        ServletContext context = event.getServletContext();
        ServletRegistration.Dynamic reg = context.addServlet(VibeServlet.class.getName(), new VibeServlet() {
            @Override
            protected Action<ServerHttpExchange> httpAction() {
                return server.httpAction();
            }
        });
        reg.setAsyncSupported(true);
        reg.addMapping("/vibe");
        // Java WebSocket API
        ServerContainer container = (ServerContainer) context.getAttribute(ServerContainer.class.getName());
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(VibeServerEndpoint.class, "/vibe")
        .configurator(new Configurator() {
            @Override
            public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
                return endpointClass.cast(new VibeServerEndpoint() {
                    @Override
                    protected Action<ServerWebSocket> wsAction() {
                        return server.wsAction();
                    }
                });
            }
        })
        .build();
        try {
            container.addEndpoint(config);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
