# Jetty Hystrix Camel - example

## To Run:
gradle run (starts jetty and camel)

## There are 2 services presented:
* http://localhost:8080/HelloWorld

  Prints Hello World to the console

* http://localhost:8080/dodgyService

  Will call a hystrix command, and return a success or failure.

* To view the hystrix serlvet output

  curl http://localhost:8080/hystrix.stream

thats it.
