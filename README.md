# Testing a sample Spring Boot REST API using REST-Assured

This project focuses on testing a sample Spring Boot REST API using [REST-assured](http://rest-assured.io/).

## Setting up the project 
Please install Maven from [here](https://maven.apache.org/install.html), if you don't have it
If using Intellij, please install the lombok plugin to fix the errors with Getter/Setter annotations
 
## Getting to know the Sample API

* Run the Spring Boot application (TestingRestApplication.class)
* Check the REST the endpoints and understand what has been implemented using Swagger (http://localhost:8080/swagger-ui.html)
* Check the H2 database (http://localhost:8080/h2-console/). Please check main/resources/application.properties for the H2 database information.

## Running the sample tests

* The tests are executed against the test database configured in test/resources/application.properties. The testing table is dropped and re-created prior to running the tests.
* Test records are inserted into the test database prior to running the API tests.  These scripts are in test/resources/data.sql 

## License
[MIT](https://choosealicense.com/licenses/mit/)
