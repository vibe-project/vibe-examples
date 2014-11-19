package simple;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.ServerHttpExchange;
import org.atmosphere.vibe.platform.server.ServerWebSocket;
import org.atmosphere.vibe.platform.server.atmosphere2.VibeAtmosphereServlet;
import org.atmosphere.vibe.server.DefaultServer;
import org.atmosphere.vibe.server.Server;
import org.atmosphere.vibe.server.ServerSocket;

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

        ServletContext context = event.getServletContext();
        ServletRegistration.Dynamic reg = context.addServlet(VibeAtmosphereServlet.class.getName(), new VibeAtmosphereServlet() {
            @Override
            protected Action<ServerHttpExchange> httpAction() {
                return server.httpAction();
            }

            @Override
            protected Action<ServerWebSocket> wsAction() {
                return server.wsAction();
            }
        });
        reg.setAsyncSupported(true);
        reg.setInitParameter(ApplicationConfig.DISABLE_ATMOSPHEREINTERCEPTOR, Boolean.TRUE.toString());
        reg.addMapping("/vibe");
        
        // Put server into application scope for JAX-RS resource to use it.
        // Otherwise, use dependency-injection service
        context.setAttribute(Server.class.getName(), server);
        // To test it's working, trigger JAX-RS resource every 3 seconds
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void tick() {
        Client client = ClientBuilder.newClient();
        client.target("http://localhost:8080/jaxrs").path("broadcast").request().post(Entity.entity("tick: " + System.currentTimeMillis(), MediaType.TEXT_PLAIN));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
