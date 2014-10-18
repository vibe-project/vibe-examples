package simple;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.ClusteredServer;
import org.atmosphere.vibe.server.ServerSocket;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.hazelcast.instance.HazelcastInstanceFactory;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final ClusteredServer server = new ClusteredServer();
        HazelcastInstance hazelcast = HazelcastInstanceFactory.newHazelcastInstance(new Config());
        final ITopic<Map<String, Object>> topic = hazelcast.getTopic("vibe");
        // Receives a message
        topic.addMessageListener(new MessageListener<Map<String, Object>>() {
            @Override
            public void onMessage(Message<Map<String, Object>> message) {
                System.out.println("receiving a message: " + message.getMessageObject());
                server.messageAction().on(message.getMessageObject());
            }
        });
        // Publishes a message
        server.publishAction(new Action<Map<String, Object>>() {
            @Override
            public void on(Map<String, Object> message) {
                System.out.println("publishing a message: " + message);
                topic.publish(message);
            }
        });
        
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
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
