package org.muckingabout.routes;

import com.jayway.restassured.response.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.muckingabout.Application;
import org.muckingabout.testClasses.IntegrationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.core.Is.is;

/**
 * @author chackney
 */
@Category(IntegrationTest.class)
public class RestApiTest {

    private static final String list_response = "[ {\r\n" +
            "  \"id\" : 123,\r\n" +
            "  \"name\" : \"Cat\"\r\n" +
            "}, {\r\n" +
            "  \"id\" : 456,\r\n" +
            "  \"name\" : \"Donald Duck\"\r\n" +
            "}, {\r\n" +
            "  \"id\" : 789,\r\n" +
            "  \"name\" : \"Micky Mouse\"\r\n" +
            "} ]";

    private static Application app;
    @BeforeClass
    public static void setup() throws Exception{
        app = new Application();
        app.configureWebServer();
        app.configureCamel();
        app.startCamel();
        app.startWebServer();
    }

    @Test
    public void requestAllPets() {
        final StringBuilder getRequestUrl = new StringBuilder()
                .append("http://").append("localhost").append(":").append("8080")
                .append("/rest/stock/findAll");

        final Response response = get(getRequestUrl.toString());

        assertThat(response.getStatusCode(), is(HttpStatus.SC_OK));

        final String responseBody = response.body().asString();
        assertEquals("Response is equal: ",list_response ,responseBody);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        app.shutdown();
    }
}
