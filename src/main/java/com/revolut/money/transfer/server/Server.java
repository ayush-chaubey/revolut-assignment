package com.revolut.money.transfer.server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);
    private static final int SERVER_PORT = 8080;
    private static final String CONTEXT_PATH = "/*";

    private static org.eclipse.jetty.server.Server getServer() {
        ApplicationConfig config = new ApplicationConfig();
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(SERVER_PORT);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, CONTEXT_PATH);
        return server;
    }

    public static void startServer() {
        LOG.info("Start Server");
        org.eclipse.jetty.server.Server server = getServer();
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("Server exception: " + e.getClass() + " " + e.getMessage());
            System.exit(1);
        } finally {
            server.destroy();
        }
    }
}
