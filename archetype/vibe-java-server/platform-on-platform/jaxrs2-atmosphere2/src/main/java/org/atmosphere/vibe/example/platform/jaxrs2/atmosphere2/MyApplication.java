package org.atmosphere.vibe.example.platform.jaxrs2.atmosphere2;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("jaxrs")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("org.atmosphere.vibe.example.platform.jaxrs2.atmosphere2");
    }
}