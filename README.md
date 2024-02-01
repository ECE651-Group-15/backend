## How to access the endpoints?

### BaseUrl: http://localhost:8080
Check in resources class for the path, for example if the path is: "/hello", then the complete path is: "http://localhost:8080/hello"

## Project architecture
Our project uses onion architecture, instead of using api, we have resources class which is under infrastructure layer.
We have two layers: 
- domain (has `service` class and `domain` objects)
- infrastructure (which has the `resource` class and `dto`s)

### What's a dto?
- A dto is a data transfer object, it's used to transfer data between layers, for example, from the resource layer to the service layer.
- It's a good practice to use dto's to avoid exposing the domain objects to the outside world.
- `dto.in` is the data FE (front-end) send to us, and `dto.out` is the data we send to the FE.
  - For example: when we create a listing, we receive `createListingDto` which is in `dto.in`, this is what FE send us.
  - And we receive the data, do some validation, save it in the db, and then send it back to FE, what we send is `listingDetailsDto` which is in `dto.out`. This is what we send to FE.

- We have a `mapper` class in the infrastructure layer, which is used to map the domain objects to dto's and vice versa, in this case, the mapper is called `toDomain` and `fromDomain` 

### What's a resource?
- A resource is a class that handles the incoming requests, it's the entry point of the application.
- It's a good practice to use resources to handle the incoming requests, and then delegate the work to the service layer.
- We have a `service` class in the domain layer, which is used to handle the business logic, in this case, the service is called `listingService`

### How to handle DB operations?
- We have a will have something like a `ListingRepository` class in the infrastructure layer, which is used to handle the db operations.

### How the flow works?
- Resources talk to service, and service talks to repository, and repository talks to the db. When db operation is done, the repository sends the data back to the service, and the service sends the data back to the resource.

### How to run the project?
- Have maven installed
- If you use intellij IDEA, go to `project structures` and config project JDK to be Java 17
- Run mvn clean install
- Run the `Main` class which is under `infrastructure`
- Quarkus supports live reload, so you can make changes and see the changes without restarting the server