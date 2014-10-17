package simple;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.atmosphere.vibe.server.Server;

public class Clock {
    // Injects the server
    @Inject
    Server server;
    
    public void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public void tick() {
        server.all().send("chat", "tick: " + System.currentTimeMillis());
    }
}