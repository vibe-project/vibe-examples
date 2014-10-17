package simple;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atmosphere.vibe.server.Server;

@Singleton
public class Clock {
    // Injects the server
    @Inject
    private Server server;
    
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