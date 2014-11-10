package org.atmosphere.samples.chat;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.VoidAction;
import org.atmosphere.vibe.platform.server.ServerHttpExchange;
import org.atmosphere.vibe.platform.server.ServerWebSocket;
import org.atmosphere.vibe.platform.server.atmosphere2.VibeAtmosphereServlet;
import org.atmosphere.vibe.server.DefaultServer;
import org.atmosphere.vibe.server.Server;
import org.atmosphere.vibe.server.ServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebListener
public class Bootstrap implements ServletContextListener {
    private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private final ObjectMapper mapper = new ObjectMapper();
    
    @SuppressWarnings("serial")
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final Server server = new DefaultServer();
        server.socketAction(new Action<ServerSocket>() {
            @Override
            public void on(final ServerSocket socket) {
                logger.info("{} is opened", socket.id());
                socket.closeAction(new VoidAction() {
                    @Override
                    public void on() {
                        logger.info("{} is closed", socket.id());
                    }
                });
                socket.errorAction(new Action<Throwable>() {
                    @Override
                    public void on(Throwable t) {
                        logger.info("{} got an error {}", socket.id(), t);
                    }
                });
                socket.on("heartbeat", new VoidAction() {
                    @Override
                    public void on() {
                        logger.info("heartbeat sent by {}", socket.id());
                    }
                });
                socket.on("message", new Action<Map<String, Object>>() {
                    @Override
                    public void on(Map<String, Object> data) {
                        Message message = mapper.convertValue(data, Message.class);
                        logger.info("{} just sent {}", message.getAuthor(), message.getMessage());
                        server.all().send("message", message);
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
        reg.addMapping("/chat");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
