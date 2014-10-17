package simple;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.Server;

import dagger.ObjectGraph;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Inject
    Server server;
    @Inject
    Clock clock;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ObjectGraph objectGraph = ObjectGraph.create(new DaggerModule());
        objectGraph.inject(this);
        // Installs the server on Atmosphere platform
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
        // Start scheduler
        clock.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}