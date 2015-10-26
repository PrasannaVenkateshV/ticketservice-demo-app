# ticketservice-demo-app
## pre-requisites 
* java 8 
* maven 3.x

## Build and Run project
* mvn clean install -  To compile and run tests.
* mvn clean package spring-boot:run - to run the application

The Project uses Spring boot as the framework for effective implementation of the TicketService.
An container based web application/service is chosen as the implementation mechanism to take advantage of the multi user and multi threading capabilities that comes with it.

Spring Boot is the goto choice because its faster to develop due to much less bolier plate code, that the developer need to handle

In memory H2 database is used here because it light weight and helps to effectively implement the service and test it.

