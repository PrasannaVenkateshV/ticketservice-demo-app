# ticketservice-demo-app
## pre-requisites 
* java 8 
* maven 3.x

## Build and Run project
* mvn clean install -  To compile and run tests.
* mvn clean package spring-boot:run - to run the application, the application uses the default http port 8080.
* some sample URLs that could be used to quickly see the behaviour of the application

http://localhost:8080/ticket-service/availableSeats?level=1

http://localhost:8080/ticket-service/holdSeats?numSeats=4&email=123@abc.com

http://localhost:8080/ticket-service/reserve?seatHoldId=1&email=123@abc.com


The Project uses Spring boot as the framework for effective implementation of the TicketService.
An container based web application/service is chosen as the implementation mechanism to take advantage of the multi user and multi threading capabilities that comes with it.

Spring Boot is my choice because its faster to develop due to much less boiler plate code, and also as an opportunity for me to learn Spring Boot.

In memory H2 database is used here because it light weight and helps to effectively implement the service and test it.

