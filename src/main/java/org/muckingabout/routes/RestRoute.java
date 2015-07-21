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
                .to("direct:pets-getAll")
            .put().description("Updates or create a pet").type(Pet.class)
                .to("direct:pets-updatePets")

            .get("/findAll").description("Find all pets")
                .outTypeList(Pet.class).to("direct:pets-listPets");

        rest("/order").id("Here to buy a pet").description("Shop for all your pets needs")
            .consumes("application/json").produces("application/json")
                .post("/place").type(Order.class)
                .description("Place an order")
                .to("bean:petService?method=placeOrder")
                .get("list")
                .to("bean:petService?method=listOrders")
                .outTypeList(Order.class);


        /**
         * Since the weave for the test dosn't work if we leave these calls as part of the main route.
         *
         * We have moved them here in there own routes.
         *
         * It actually mackes it a tad neater anyways, these could easily be moved to another class, and
         * which logically they belong too.
         */
        from("direct:pets-listPets")
                .onCompletion().log("the End").id("pets-listPets-end").end() // here to bind in test -#Rubbish
                .routeId("pets-listPets")
                .to("bean:petService?method=listPets")
                .id("beanlistPets");

        from("direct:pets-updatePets")
                .onCompletion().log("the End").id("pets-updatePets-end").end() // here to bind in test -#Rubbish
                .routeId("pets-updatePets")
                .to("bean:petService?method=updatePet")
                .id("beanupdatePets");

        from("direct:pets-getAll")
                .onCompletion().log("the End").id("pets-getAll-end").end() // here to bind in test -#Rubbish
                .routeId("pets-getAll").to("bean:petService?method=getPet(${header.id})").id("beanGetPets");

    }
}
