# Second Stage of Technical Coding Interview

## The requirements 

What I have to do:

- Rest API for Customers. Each customer have to be defined with 
    -- First name
    -- Last name
    -- National Document of Identity
    -- Image (optional)
    -- With each customer also store last purchases information

- Only admin roles can access the API. Use OAuth2 to check

- Optionally, one or more cucumber tests to check one or more end-points


## API URIs

 
| URN | VERB | Purpose |
| --- | --- | --- |
| /customers  | GET | List all customers (if use query param "sorted", the result will be in order) |
| /customers/:id | GET | Gets data for the customer with :id |
| /customers | POST | Adds a new customer (when data is correct) |
| /customers | PUT | Change base information of the customer |
| /customers/:id | DELETE | Deletes the customer with the :id |
| /customers/:id | OPTIONS | Checks if a customer exists with that :id |
| /customers/:id/image | GET | Gets the image associated with the customer |
| /customers/:id/image | POST | Store a new (or substitute) the image associated with the customer |
| /customers/:id/image | PUT | Does the same of POST case |
| /customers/:id/image | DELETE | Erases the image of the customer |
| /customers/:id/purchases | GET | List all purchases of the customer |
| /customers/:id/purchases/:idpurchase | GET | Gets information of the selected purchase |
| /customers/:id/purchases | POST | Insert a new purchase in the list of purchases of a customer |
| /customers/:id/purchases | PUT | Change information of a exist purchase in the list of a customer |
| /customers/:id/purchases/:idpurchase | DELETE | Removes a purchase from the list of purchases of a customer |


## Downloading, compiling and executing 

Prepare, if you haven't, the environment installing Java 8 and Maven.

Download the code to a local folder with git, and go to the folder:

```bash
git clone https://github.com/saulo-alvarado/theam.io-api-rest-stage-2.git
cd theam.io-api-rest-stage-2
```

Build: 

```bash
mvn clean
mvn package
```

Run the API using (Linux/Windows/Mac)

```bash
java -jar ./rest-api/target/rest-api-customers.jar
```

Linux users should use shell script existing in root project folder:

```bash
./run.sh
```

Now you can interact with it (some user preexist because they are hardcoded).

To end the session you can break the execution with `CTRL+C` or you can send a request to shutdown using the namesake URN REST API resource (see below). 


## Simple use

Generally I prefer [HTTPie](https://httpie.org/) instead CURL. These are examples of use the API with HTTPie. 

* List of customers (**http://localhost:4567/customers**):

    ```bash
    http [GET] http://localhost:4567/customers
    ```

* Get data for first customer (id 1) (**http://localhost:4567/customers/1**):

    ```bash
    http [GET] http://localhost:4567/customers/1
    ```
    
* Create a new customer (**http://localhost:4567/customers** with **POST verb**):
    
    ```bash
    http POST http://localhost:4567/customers firstName=Saulo lastName=Alvarado nid=99999999X
    ```
    
* Change/Edit data of a customer (**http://localhost:4567/customers** with **PUT verb**):

     ```bash
     http PUT http://localhost:4567/customers id=1 firstName=Saulo lastName="Alvarado Mateos" nid=99999999X
     ```

* Delete first customer (**http://localhost:4567/customers/1** with **DELETE verb**):

    ```bash
    http DELETE http://localhost:4567/customers/1
    ```

* To end the session:

    ```bash
    http [GET] http://localhost:4567/shutdown
    ```
