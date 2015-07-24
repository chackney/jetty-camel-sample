# Jetty Hystrix Camel - example

## To Run:
gradle run (starts jetty and camel- no spring boot)

## The following services are present
* http://localhost:8080/HelloWorld

  Prints Hello World to the console

* http://localhost:8080/dodgyService

  Will call a hystrix command, and return a success or failure.

* To view the hystrix serlvet output

  curl http://localhost:8080/hystrix.stream

  (this won't start until the dodgyService above has been hit at least once')

* To view the swagger api, load swagger (can be found here http://swagger.io/), and point it at the following url

   http://localhost:8080/api

This will load the api defined in the RestRouteServlet.

There are also a set of exmaple tests:
RestRouteTest - tests the route component.
RestApiTest - Starts the server a runs http test against
RestApiTestStubMode - starts the server in Stub mode, and runs http test against it.

thats it.
