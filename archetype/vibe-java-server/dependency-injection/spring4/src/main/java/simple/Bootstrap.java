package simple;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.atmosphere.vibe.platform.server.atmosphere2.AtmosphereBridge;
import org.atmosphere.vibe.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    @SuppressWarnings("resource")
    public void contextInitialized(ServletContextEvent event) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        // Installs the server on Atmosphere platform
        Server server = applicationContext.getBean(Server.class);
        new AtmosphereBridge(event.getServletContext(), "/vibe").httpAction(server.httpAction()).websocketAction(server.websocketAction());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}