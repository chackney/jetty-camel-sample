package org.muckingabout.routes;

import org.apache.camel.builder.RouteBuilder;
import org.muckingabout.serviceactivator.MyReallyDodgyServiceCommand;

/**
 * @author chackney
 */
public class HystrixRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("servlet://other/dodgyService?servletName=CamelServlet")
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
