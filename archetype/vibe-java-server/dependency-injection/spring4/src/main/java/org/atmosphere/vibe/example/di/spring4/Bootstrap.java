package org.atmosphere.vibe.example.di.spring4;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.vibe.Server;
import org.atmosphere.vibe.platform.action.Action;
import org.atmosphere.vibe.platform.bridge.atmosphere2.VibeAtmosphereServlet;
import org.atmosphere.vibe.platform.http.ServerHttpExchange;
import org.atmosphere.vibe.platform.ws.ServerWebSocket;
import org.atmosphere.vibe.transport.http.HttpTransportServer;
import org.atmosphere.vibe.transport.ws.WebSocketTransportServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    @SuppressWarnings({ "resource", "serial" })
    public void contextInitialized(ServletContextEvent event) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        final Server server = applicationContext.getBean(Server.class);
        
        final HttpTransportServer httpTransportServer = new HttpTransportServer().transportAction(server);
        final WebSocketTransportServer wsTransportServer = new WebSocketTransportServer().transportAction(server);

        // Installs the server on Atmosphere platform
        ServletContext context = event.getServletContext();
        ServletRegistration.Dynamic reg = context.addServlet(VibeAtmosphereServlet.class.getName(), new VibeAtmosphereServlet() {
            @Override
            protected Action<ServerHttpExchange> httpAction() {
                return httpTransportServer;
            }

            @Override
            protected Action<ServerWebSocket> wsAction() {
                return wsTransportServer;
            }
        });
        reg.setAsyncSupported(true);
        reg.setInitParameter(ApplicationConfig.DISABLE_ATMOSPHEREINTERCEPTOR, Boolean.TRUE.toString());
        reg.addMapping("/vibe");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}