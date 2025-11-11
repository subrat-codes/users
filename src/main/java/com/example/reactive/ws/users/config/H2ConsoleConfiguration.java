package com.example.reactive.ws.users.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
//@Profile({"dev", "test"})
@Profile("!prod & !production")
@Slf4j
public class H2ConsoleConfiguration {

    private Server webServer;

    @EventListener(ApplicationStartedEvent.class)
    public void start() throws java.sql.SQLException {
        String webPort = "8082";
        this.webServer = Server.createWebServer("-webPort", webPort).start();
        log.info("H2 console available at http://localhost:" + webPort);
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.webServer.stop();
        log.info("H2 console stopped");
    }

}
