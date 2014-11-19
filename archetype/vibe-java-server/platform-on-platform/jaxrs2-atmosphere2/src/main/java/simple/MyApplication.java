package simple;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("jaxrs")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("simple");
    }
}