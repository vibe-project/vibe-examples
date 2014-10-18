package simple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.atmosphere.vibe.platform.Action;
import org.atmosphere.vibe.platform.server.vertx2.VertxBridge;
import org.atmosphere.vibe.server.ClusteredServer;
import org.atmosphere.vibe.server.ServerSocket;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.platform.Verticle;

public class Bootstrap extends Verticle {
    @Override
    public void start() {
        final ClusteredServer server = new ClusteredServer();
        // You need to set cluster configuration from vertx-maven-plugin to true to enable distributed event bus
        final EventBus eventBus = vertx.eventBus();
        // Receives a message
        eventBus.registerHandler("vibe", new Handler<Message<byte[]>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(Message<byte[]> message) {
                // Message's body's type bytes generated from Map object
                // retrieve original object
                ByteArrayInputStream bais = new ByteArrayInputStream(message.body());
                Map<String, Object> body = null;
                try (ObjectInputStream in = new ObjectInputStream(bais)) {
                    body = (Map<String, Object>) in.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("receiving a message: " + body);
                server.messageAction().on(body);
            }
        });
        // Publishes a message
        server.publishAction(new Action<Map<String,Object>>() {
            @Override
            public void on(Map<String, Object> message) {
                System.out.println("publishing a message: " + message);
                // EventBus doesn't allow to publish object though it is Serializable
                // so convert it to byte array and publish it instead
                ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
                try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                    out.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                eventBus.publish("vibe", baos.toByteArray());
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
        HttpServer httpServer = vertx.createHttpServer();
        new VertxBridge(httpServer, "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
        httpServer.listen(Integer.parseInt(System.getProperty("vertx.port")));
    }
}