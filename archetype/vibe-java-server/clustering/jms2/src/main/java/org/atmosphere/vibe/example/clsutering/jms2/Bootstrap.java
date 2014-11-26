package org.atmosphere.vibe.example.clsutering.jms2;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.ServerHttpExchange;
import org.atmosphere.vibe.platform.server.ServerWebSocket;
import org.atmosphere.vibe.platform.server.atmosphere2.VibeAtmosphereServlet;
import org.atmosphere.vibe.server.ClusteredServer;
import org.atmosphere.vibe.server.ServerSocket;

@WebListener
public class Bootstrap implements ServletContextListener {
    @SuppressWarnings("serial")
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final ClusteredServer server = new ClusteredServer();
        try {
            // Set by HornetQ standalone server
            Properties props = new Properties();
            props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
            props.put("java.naming.provider.url", "jnp://localhost:1099");
            InitialContext initialContext = new InitialContext(props);
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext.lookup("ConnectionFactory");
            Topic topic = (Topic) initialContext.lookup("topic/vibe");
            TopicConnection connection = connectionFactory.createTopicConnection();
            final TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            // Receives a message
            TopicSubscriber subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(new MessageListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("receiving a message: " + message.getBody(Map.class));
                        server.messageAction().on(message.getBody(Map.class));
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Publishes a message
            final TopicPublisher publisher = session.createPublisher(topic);
            server.publishAction(new Action<Map<String, Object>>() {
                @Override
                public void on(Map<String, Object> message) {
                    System.out.println("publishing a message: " + message);
                    try {
                        publisher.publish(session.createObjectMessage((Serializable) message));
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        
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
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
