package org.muckingabout.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpStatus;

import com.rmg.camel.exception.IrrecoverableException;

/**
 * Created by CLHACKNE on 14/07/2015.
 */
public class HelloWorldRoot extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("servlet://muckingAbout/helloWorld?servletName=camelHttpTransportServlet")
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