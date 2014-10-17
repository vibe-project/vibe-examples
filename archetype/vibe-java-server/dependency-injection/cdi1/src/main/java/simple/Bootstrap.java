package simple;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.Server;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Inject
    private Server server;
    // Just for eager instantiation of Clock instance
    @SuppressWarnings("unused")
    @Inject
    private Clock clock;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Installs the server on Atmosphere platform
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}