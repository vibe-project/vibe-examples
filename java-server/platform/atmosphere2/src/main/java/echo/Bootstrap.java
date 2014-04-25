package echo;

import io.react.Action;
import io.react.atmosphere.AtmosphereBridge;
import io.react.runtime.DefaultServer;
import io.react.runtime.Server;
import io.react.runtime.Socket;

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
		
		new AtmosphereBridge(event.getServletContext(), "/react").httpAction(server.httpAction()).websocketAction(server.websocketAction());
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
