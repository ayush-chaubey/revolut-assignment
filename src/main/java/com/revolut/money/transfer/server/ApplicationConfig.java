package com.revolut.money.transfer.server;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("resources")
class ApplicationConfig extends ResourceConfig {

    ApplicationConfig() {
        packages("com.revolut.money.transfer");
        register(JacksonFeature.class);
    }
}
