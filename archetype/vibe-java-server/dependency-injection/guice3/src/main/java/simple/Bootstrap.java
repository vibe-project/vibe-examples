package simple;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.Server;

import com.google.inject.Guice;
import com.google.inject.Injector;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Injector injector = Guice.createInjector(new GuiceModule());
        // Installs the server on Atmosphere platform
        Server server = injector.getInstance(Server.class);
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
        // Start scheduler
        injector.getInstance(Clock.class).init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}