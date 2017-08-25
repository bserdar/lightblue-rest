package com.redhat.lightblue.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception
    {
            Server server = new Server(8080);
            
            ServletContextHandler handler = new ServletContextHandler();
            server.setHandler(handler);

            handler.addServlet(org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher.class,"/");
            handler.setInitParameter("javax.ws.rs.Application","com.redhat.lightblue.rest.RestApplication");
            
            // WebAppContext context = new WebAppContext();

            // context.setResourceBase("/rest");
            // context.setContextPath("/");
            // context.setParentLoaderPriority(true);            
            // server.setHandler(context);
            server.start();
            server.join();
    }

}
