package org.atmosphere.vibe.example.platform.servlet3_jwa1;

import javax.servlet.Servlet;
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
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;

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
        
        HttpTransportServer httpTransportServer = new HttpTransportServer().transportAction(server);
        // Servlet
        ServletContext context = event.getServletContext();
        Servlet servlet = new VibeServlet().httpAction(httpTransportServer);
        ServletRegistration.Dynamic reg = context.addServlet(VibeServlet.class.getName(), servlet);
        reg.setAsyncSupported(true);
        reg.addMapping("/vibe");
        
        final WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().transportAction(server);
        // Java WebSocket API
        ServerContainer container = (ServerContainer) context.getAttribute(ServerContainer.class.getName());
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(VibeServerEndpoint.class, "/vibe")
        .configurator(new Configurator() {
            @Override
            public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
                return endpointClass.cast(new VibeServerEndpoint().wsAction(wsTransportServer));
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
