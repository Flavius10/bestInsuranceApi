**BestInsuranceApi**



BestInsuranceApi is a REST API built with Java 17 and Spring Boot,

designed to manage customers, policies, coverages, and subscriptions in

an insurance domain.

The project leverages Docker and Docker Compose for easy setup and

provides sample data to simplify testing and development.



------------------------------------------------------------------------



**Setup**



Requirements



\-   JDK 17 (any distribution, e.g., Amazon Corretto, OpenJDK)

\-   Docker

\-   Docker Compose



If you are on a Mac M1, set the following environment variable before

starting containers:



&nbsp;   export DOCKER\_DEFAULT\_PLATFORM=linux/amd64



Running the project



You can start the application using the provided scripts:



&nbsp;   ./run\_maven.sh

&nbsp;   or

&nbsp;   ./run\_gradle.sh



For Windows users:



&nbsp;   run\_maven.bat

&nbsp;   or

&nbsp;   run\_gradle.bat



The project will start all required containers (including the database)

and load the application.



------------------------------------------------------------------------



**Test Data**



\-   Sample data for customers, policies, coverages, and subscriptions is

&nbsp;   created at startup by

&nbsp;   com.bestinsurance.api.services.SampleDataLoader (configured in

&nbsp;   com.bestinsurance.api.config.DomainConfig).



To disable sample data creation, set the property

DATALOADER\_LOADSAMPLE=false (or remove it) in docker/docker-compose.yml.



In src/test/resources you will also find CSV files to support

integration and edge-case testing: - customers.csv – valid test data -

customers\_emailtest.csv – contains duplicate emails for testing conflict

handling - customers\_nocityerror.csv – contains an invalid city

reference - customers\_nostateerror.csv – contains an invalid state

reference



------------------------------------------------------------------------



**Code Structure**



The project is organized into three main layers following a DTO +

Service + Repository pattern:



\-   REST layer (com.bestinsurance.api.rest)

&nbsp;   Defines REST endpoints and handles mapping between DTOs and domain

&nbsp;   objects.

&nbsp;   -   CrudController (generic CRUD interface)

&nbsp;   -   AbstractCrudController \& AbstractSimpleIdCrudController (base

&nbsp;       implementations)

&nbsp;   -   Controllers: CustomerController, PolicyController,

&nbsp;       CoverageController, SubscriptionController

\-   Service layer (com.bestinsurance.api.services)

&nbsp;   Contains business logic and persistence handling.

&nbsp;   -   CrudService interface

&nbsp;   -   AbstractCrudService (base class with update pre-save logic)

&nbsp;   -   Services: CustomerService, PolicyService, CoverageService,

&nbsp;       SubscriptionService

\-   Persistence layer (com.bestinsurance.api.repos)

&nbsp;   Implements persistence with Spring Data JPA repositories.



Additional directories: - src/main/resources – Configuration files

(including Liquibase changelogs)

\- src/test – Unit and integration tests

\- docker – Docker and Compose configuration files



------------------------------------------------------------------------



**Contributing**



Contributions are welcome!

Please open an issue to discuss proposed changes before submitting a

pull request.



------------------------------------------------------------------------



**Contact**



Maintained by Flavius10.

Feel free to open issues on GitHub for questions or suggestions.



