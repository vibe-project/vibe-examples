package org.atmosphere.samples.chat;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.VoidAction;
import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
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
        new AtmosphereBridge(event.getServletContext(), "/chat").httpAction(server.httpAction()).websocketAction(server.websocketAction());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
