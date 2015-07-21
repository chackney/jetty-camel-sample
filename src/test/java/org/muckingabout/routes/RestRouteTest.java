package org.muckingabout.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.muckingabout.model.Pet;
import org.muckingabout.serviceactivator.PetService;
import org.muckingabout.testClasses.AcceptanceTest;

/**
 * Created by CLHACKNE on 21/07/2015.
 */
@Category(AcceptanceTest.class)
public class RestRouteTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @EndpointInject(uri = "direct:in")
    protected DirectEndpoint directIn;

    @Produce
    protected ProducerTemplate template;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry =  super.createRegistry();
        jndiRegistry.bind("petService",new PetService());
        return jndiRegistry;
    }

    @Before
    public void setup() throws Exception {
        context.getRouteDefinition("route3")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        replaceFromWith("direct:in");
                    }
                });
        context.getRouteDefinition("pets-listPets")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveById("pets-listPets-end").after().to("mock:result");
                    }
                });
        context().start();
    }

    @Test
    public void testSendMatchingMessage() throws Exception {
        String expectedBody = "[ {\r\n" +
                "  \"id\" : 123,\r\n" +
                "  \"name\" : \"Cat\"\r\n" +
                "}, {\r\n" +
                "  \"id\" : 456,\r\n" +
                "  \"name\" : \"Donald Duck\"\r\n" +
                "}, {\r\n" +
                "  \"id\" : 789,\r\n" +
                "  \"name\" : \"Micky Mouse\"\r\n" +
                "} ]";

        resultEndpoint.expectedBodiesReceived(expectedBody.trim());
        template.sendBodyAndHeader(directIn, "", "", "");

        System.out.println(resultEndpoint.getExchanges().get(0).getOut().getBody());
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
      return  new RestRoute();
    }

}
