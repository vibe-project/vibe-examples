package org.atmosphere.vibe.example.di.guice3;

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

import com.google.inject.Guice;
import com.google.inject.Injector;

@WebListener
public class Bootstrap implements ServletContextListener {
    @SuppressWarnings("serial")
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Injector injector = Guice.createInjector(new GuiceModule());
        // Installs the server on Atmosphere platform
        final Server server = injector.getInstance(Server.class);
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
        // Start scheduler
        injector.getInstance(Clock.class).init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}