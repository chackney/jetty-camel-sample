package org.muckingabout;

import java.util.HashMap;
import java.util.Map;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.component.swagger.DefaultCamelSwaggerServlet;
import org.apache.camel.component.swagger.RestSwaggerCorsFilter;
import org.apache.camel.main.Main;
import org.muckingabout.http.JettyServerImpl;
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

           /**
             * The server were using
             * Again in future change this to factory/discovery method then
             * update this.
             */
            HttpServerWrapper server = new JettyServerImpl(8080);
            // create a Main Camel instance
            Main main = new Main();
            try {

                server.addServlet("CamelServlet", "/rest/*", new CamelHttpTransportServlet(), null);
                server.addServlet("HystrixServlet", "/hystrix.stream/*", new HystrixMetricsStreamServlet(), null);
                server.addServlet("apiServlet", "/api/*", new DefaultCamelSwaggerServlet(), createSwaggerServletinitConfig());
                String [] paths = {"/api/*", "/rest/*"};
                server.addFilter("restSwaggerCorsFilter", new RestSwaggerCorsFilter(),paths );


                // enable hangup support so you can press ctrl + c to terminate the JVM
                main.enableHangupSupport();
                // bind petService into the registery - Basic Dependancy Injection
                main.bind("petService", new PetService());
                // add routes
                main.addRouteBuilder(new HelloWorldRoot());
                main.addRouteBuilder(new HystrixRoute());
                main.addRouteBuilder(new RestRoute());

                // Start camel
                main.start();
                while (!main.isStarted()) {
                    Thread.sleep(10);
                }
                // Start things up! - this also blocks
                server.startServer();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                server.stopServer();
                main.stop();
            }

        }


    private static Map<String,String> createSwaggerServletinitConfig() {

        HashMap configParameters = new HashMap();

        configParameters.put("base.path", "rest");
        configParameters.put("api.path", "api");
        configParameters.put("api.version", "0.1");
        configParameters.put("api.title", "Basic Pet Store");
        configParameters.put("api.description", "Get your lovely pets here");

        return configParameters;
    }






}
