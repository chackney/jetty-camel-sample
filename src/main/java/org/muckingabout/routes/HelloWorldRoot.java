package org.muckingabout.routes;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author chackney
 */
public class HelloWorldRoot extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("servlet://other/helloWorld?servletName=CamelServlet")
                .routeId("helloWorldRoute")
                .bean(new SomeBean())
                .convertBodyTo(String.class)
                .end();
    }


    public static class SomeBean {

        public void someMethod(String body) {
            System.out.println("Hello World");
        }
    }
}