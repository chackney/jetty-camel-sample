package org.muckingabout;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.component.swagger.DefaultCamelSwaggerServlet;
import org.apache.camel.component.swagger.RestSwaggerCorsFilter;
import org.apache.camel.main.Main;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.muckingabout.routes.HelloWorldRoot;
import org.muckingabout.routes.HystrixRoute;
import org.muckingabout.routes.RestRoute;
import org.muckingabout.serviceactivator.PetService;

/**
 *
 * @author chackey
 */
public class Application {

        public static void main( String[] args ) throws Exception
        {

            Server server = new Server(8080);


            // Setup JMX
            MBeanContainer mbContainer = new MBeanContainer(
                    ManagementFactory.getPlatformMBeanServer());
            server.addBean(mbContainer);


            ServletHandler servletHandler = new ServletHandler();
            ServletHolder camelServlet = new ServletHolder();
            camelServlet.setName("CamelServlet");
            camelServlet.setServlet(new CamelHttpTransportServlet());
            servletHandler.addServletWithMapping(camelServlet,"/rest/*");

            ServletHolder hystrixServlet = new ServletHolder();
            hystrixServlet.setName("HystrixServlet");
            hystrixServlet.setServlet(new HystrixMetricsStreamServlet());

            servletHandler.addServletWithMapping(hystrixServlet,"/hystrix.stream");
            ServletHolder apiServlet = new ServletHolder();
            apiServlet.setName("apiServlet");
            apiServlet.setServlet(new DefaultCamelSwaggerServlet());

            apiServlet.setInitParameter("base.path","rest");
            apiServlet.setInitParameter("api.path","api" );
            apiServlet.setInitParameter("api.version","0.1" );
            apiServlet.setInitParameter("api.title","Basic Pet Store");
            apiServlet.setInitParameter("api.description","Get your lovely pets here");

            servletHandler.addServletWithMapping(apiServlet,"/api/*");
            FilterHolder filterHolder = new FilterHolder();
            filterHolder.setName("restSwaggerCorsFilter");
            filterHolder.setFilter(new RestSwaggerCorsFilter());

            String [] filterPaths = {"/api/*","/rest/*"} ;
            FilterMapping filterMapping = new FilterMapping();
            filterMapping.setPathSpecs(filterPaths);
            filterMapping.setFilterName("restSwaggerCorsFilter");
            servletHandler.addFilter(filterHolder,filterMapping);
            // A WebAppContext is a ContextHandler as well so it needs to be set to
            // the server so it is aware of where to send the appropriate requests.
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            context.setServletHandler(servletHandler);
            server.setHandler(context);

            // create a Main instance
            Main main = new Main();
            // enable hangup support so you can press ctrl + c to terminate the JVM
            main.enableHangupSupport();
            // bind MyBean into the registery
            main.bind("petService", new PetService());
            // add routes
            main.addRouteBuilder(new HelloWorldRoot());
            main.addRouteBuilder(new HystrixRoute());
            main.addRouteBuilder(new RestRoute());

            // run until you terminate the JVM
            System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
            main.start();
            while(!main.isStarted()) {
                Thread.sleep(10);
            }
            // Start things up!
            server.start();

            server.join();
        }



}
