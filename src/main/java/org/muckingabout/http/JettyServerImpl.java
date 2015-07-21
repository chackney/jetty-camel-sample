package org.muckingabout.http;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.lang.management.ManagementFactory;
import java.util.Map;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.muckingabout.HttpServerWrapper;

/**
 * @author chackney
 */
public class JettyServerImpl implements HttpServerWrapper {

    private Server server;
    private ServletHandler servletHandler;

    public JettyServerImpl(int port) {
        server = new Server(port);
        servletHandler = new ServletHandler();
        addJMX(server);

    }


    @Override
    public void startServer() throws Exception{

        ServletContextHandler context = createServeltContext(this.servletHandler);
        server.setHandler(context);
        server.start();

    }

    @Override
    public void stopServer() throws Exception{
       server.stop();
    }

    private static ServletContextHandler createServeltContext(ServletHandler servletHandler) {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.setServletHandler(servletHandler);
        return context;
    }
    @Override
    public void addServlet(String servletName, String path, HttpServlet httpServlet, Map<String,String> initConfig) {
        ServletHolder servlet = new ServletHolder();

        servlet.setName(servletName);
        servlet.setServlet(httpServlet);
        servletHandler.addServletWithMapping(servlet,path);
        if(initConfig != null) {
            for(String key : initConfig.keySet()) {
                servlet.setInitParameter(key,initConfig.get(key));
            }
        }
    }

    @Override
    public void addFilter(String servletName,  Filter httpFilter, String[] paths) {
        FilterHolder filterHolder = new FilterHolder();
        filterHolder.setName(servletName);
        filterHolder.setFilter(httpFilter);
        FilterMapping filterMapping = new FilterMapping();
        filterMapping.setPathSpecs(paths);
        filterMapping.setFilterName(servletName);
        servletHandler.addFilter(filterHolder,filterMapping);
    }

    public void block() throws Exception{
        server.join();
    }
    private void addJMX(Server server) {
        // Setup JMX
        MBeanContainer mbContainer = new MBeanContainer(
                ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);
    }


}
