# MachineFactory
An order system for a factory that sells machines

Started by setting up my spring initializr att https://start.spring.io/.
i chose Project maven, language Java and springboot 3.2.0.
I then included the following dependencies on Spring Initializr:
Spring Web: For building web applications, including RESTful services.
Spring Data JPA: To easily implement JPA based repositories and database interactions.
H2 Database: An in-memory database, useful for development and testing.
Spring Boot DevTools: For fast application restarts, LiveReload, and configurations for enhanced development experience.
Lombok: A Java library that automatically plugs into the editor and build tools, spicing up the Java code. to reduce boilerplate code, especially for model/data objects.
Validation: For bean validation using the Java Bean Validation API.
Spring Security: To add security features to your application.
Actuator: To monitor and manage your application.
Flyway: For database migration. because I plan to evolve the database schema in a controlled and versioned manner later on.

I then open the folder in intelij and after i build my project to make sure everyting was setted up correctly, I then created a uml with help of my plugin PlanUML.
I focused on creating a class diagram that represents the domain model. This includes entities like Customer, Address, Order, Machine, Subassembly, and Part,
along with their relationships mention in the assignment.  as following:
Customer Class:
Attributes: customerId, name, email,
Relationships:
One-to-Many with Order.
One-to-Many with Address (bi-directional).

Address Class:
Attributes: addressId, street, city, zipCode,
Relationships:
Many-to-One with Customer (bi-directional).

Order Class:
Attributes: orderId, orderDate, status,
Relationships:
Many-to-One with Customer.
One-to-Many with Machine.

Machine Class:
Attributes: machineId, type, model,
Relationships:
One-to-Many with Subassembly.

Subassembly Class:
Attributes: subassemblyId, name,
Relationships:
One-to-Many with Part.

Part Class:
Attributes: partId, partName, manufacturer,

after that i initiated my first git commit.
 
#sourcers:
https://www.baeldung.com/spring-boot-h2-database
https://www.bezkoder.com/spring-boot-jpa-h2-example/
https://www.youtube.com/watch?v=-H5sud1-K5A
https://my-json-server.typicode.com/
