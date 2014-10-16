package simple;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@WebListener
public class Bootstrap extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.setInitParameter(CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
        servletContext.setInitParameter(CONFIG_LOCATION_PARAM, this.getClass().getPackage().getName());
        super.contextInitialized(event);
    }
}