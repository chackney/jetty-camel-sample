package org.muckingabout.routes;

import org.apache.camel.builder.RouteBuilder;
import org.muckingabout.serviceactivator.MyReallyDodgyServiceCommand;

/**
 * Created by CLHACKNE on 14/07/2015.
 */
public class HystrixRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("servlet://muckingAbout/dodgyService?servletName=camelHttpTransportServlet")
                .routeId("dodgyRoute")
                .bean(new HystrixBean())
                .convertBodyTo(String.class)
                .end();
    }

    public static class HystrixBean {

        public String runTheHystrixCommand(String body) {
            System.out.println("Hello World");
            return new MyReallyDodgyServiceCommand().execute();
        }
    }
}
