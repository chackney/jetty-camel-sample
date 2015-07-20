# Jetty Hystrix Camel - example

## To Run:
gradle run (starts jetty and camel)

## The following services are present
* http://localhost:8080/HelloWorld

  Prints Hello World to the console

* http://localhost:8080/dodgyService

  Will call a hystrix command, and return a success or failure.

* To view the hystrix serlvet output

  curl http://localhost:8080/hystrix.stream

  (this won't start until the dodgyService above has been hit at least once')

* To view the swagger api, load swagger, and point it at the following url

 curl http://localhost:8080/api

thats it.
