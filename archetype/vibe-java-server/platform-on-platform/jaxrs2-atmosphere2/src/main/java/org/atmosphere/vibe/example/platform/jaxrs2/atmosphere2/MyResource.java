package org.atmosphere.vibe.example.platform.jaxrs2.atmosphere2;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.atmosphere.vibe.server.Server;

@Path("broadcast")
public class MyResource {
    // If JAX-RS resources are deployed on Servlet containers, ServletContext is
    // injectable according to JAX-RS spec.
    @Context
    private ServletContext context;

    @POST
    public void broadcast(String message) {
        // Retrieves a vibe server from application scope
        Server server = (Server) context.getAttribute(Server.class.getName());
        server.all().send("chat", message);
    }
}
