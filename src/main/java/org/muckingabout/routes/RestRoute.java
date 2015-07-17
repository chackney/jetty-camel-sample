package org.muckingabout.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.muckingabout.model.Order;
import org.muckingabout.model.Pet;

/**
 * @author chackney
 */
public class RestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure we want to use servlet as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/rest").port(8080);

        // this user REST service is json only
        rest("/stock").description("Pet rest service")
            .consumes("application/json").produces("application/json")

            .get("/{id}").description("Find pet by id").outType(Pet.class)
                .to("bean:petService?method=getPet(${header.id})")

            .put().description("Updates or create a pet").type(Pet.class)
                .to("bean:petService?method=updatePet")

            .get("/findAll").description("Find all pets").outTypeList(Pet.class)
                .to("bean:petService?method=listPets");

        rest("order").id("Here to buy a pet").description("Shop for all your pets needs")
                .consumes("application/json")
                .produces("application/json")
                .post("/place").type(Order.class)
                .description("Place an order")
                .to("bean:petService?method=placeOrder")
                .get("list").outTypeList(Order.class).to("bean:petService?method=listOrders");


    }
}
