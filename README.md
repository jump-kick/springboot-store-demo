# Springboot REST API Demo
You can build/run the program with Maven, using the command, 'mvn spring-boot:run'.

Navigate to localhost:8080/swagger-ui/index.html for the REST endpoints.

This is demo of a REST API that controls an online shopping system.  A client can use the manager endpoints to create and update products and apply deals.  Using the shopper endpoints, you can add products (created by the manager endpoints) to your basket, update your basket, and get the total.  The basket automatically calculates any deals that the manager has activated.