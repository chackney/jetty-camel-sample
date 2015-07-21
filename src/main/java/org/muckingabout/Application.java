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
import org.muckingabout.serviceactivator.stub.PetServiceStub;

/**
 * @author chackey
 */
public class Application {

    /**
     * The server were using
     * Again in future change this to factory/discovery method then
     * update this.
     */
    HttpServerWrapper server = new JettyServerImpl(8080);
    // create a Main Camel instance
    Main main = new Main();

    public Application() {
    }

    public void configureWebServer() {
        this.server.addServlet("CamelServlet", "/rest/*", new CamelHttpTransportServlet(), null);
        this.server.addServlet("HystrixServlet", "/hystrix.stream/*", new HystrixMetricsStreamServlet(), null);
        this.server.addServlet("apiServlet", "/api/*", new DefaultCamelSwaggerServlet(), createSwaggerServletinitConfig());
        String[] paths = {"/api/*", "/rest/*"};
        this.server.addFilter("restSwaggerCorsFilter", new RestSwaggerCorsFilter(), paths);
    }

    public void configureCamel () {
        // enable hangup support so you can press ctrl + c to terminate the JVM
        this.main.enableHangupSupport();
        // bind petService into the registery - Basic Dependancy Injection
        // basic profile selection :)
        String profile = System.getProperty("profile");
        if("stub".equals(profile)) {
            profileStub();
        } else {
            profileLive();
        }
        // add routes
        this.main.addRouteBuilder(new HelloWorldRoot());
        this.main.addRouteBuilder(new HystrixRoute());
        this.main.addRouteBuilder(new RestRoute());
    }

    public void startCamel() throws Exception {
        // Start camel
        this.main.start();
        while (!main.isStarted()) {
            Thread.sleep(10);
        }

    }
    public void startWebServer() throws Exception{
        this.server.startServer();

    }

    public void block() throws Exception {
        server.block();
    }

    public void shutdown() throws Exception{
        this.server.stopServer();
        this.main.stop();
    }

    public static void main(String[] args) throws Exception {

        Application application = new Application();
        try {
            application.configureWebServer();
            application.configureCamel();
            application.startCamel();
            application.startWebServer();
            application.block();
       } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            application.shutdown();
        }

    }

    /**
     * These should get moved, as you add more beans
     * you'll need something a bit more elegant :)
     */
    private void profileStub() {
        this.main.bind("petService", new PetServiceStub());
    }

    private void profileLive() {
        this.main.bind("petService", new PetService());
    }

    private static Map<String, String> createSwaggerServletinitConfig() {

        HashMap configParameters = new HashMap();

        configParameters.put("base.path", "rest");
        configParameters.put("api.path", "api");
        configParameters.put("api.version", "0.1");
        configParameters.put("api.title", "Basic Pet Store");
        configParameters.put("api.description", "Get your lovely pets here");

        return configParameters;
    }


}
