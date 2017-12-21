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


## The Fast Lane

Despite my wishes, I don't have too much time. The Internet is plagued with a lot of examples and projects that solve similar cases. Copying and pasting (and gutting and recomposing too) is a common practice and approach in the profession. Google `spring+boot+oauth2` and you'll get a lot of examples. Here my contribution to increase the Entropy of Universe. Basically the important part of the code was ~~ripped~~ adapted from [Example Spring Boot REST API](https://github.com/gigsterous/gigy-example).

I really hope to find time (and feel like) to continue with the master branch and keep trying things.


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

Generally I prefer [HTTPie](https://httpie.org/) instead CURL. It's recommendable to install [httpie-jwt-auth](https://github.com/teracyhq/httpie-jwt-auth) plugin. In current version I use the JWT token simulation to protect the API.   

### API URIs

These are the resources offered by the API.
 
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


### Before call

With each call to the API you need to include an oauth token. To get one, use the next command line operation:

```bash
http -v -f -a 'client-id:client-secret' POST http://localhost:8080/theam/oauth/token 'grant_type=password' 'username=saulo.alvarado' 'password=password'
```

Copy the token (only the text) and set environment variable `JWT_AUTH_TOKEN` with previous value:


```bash
export JWT_AUTH_VALUE=eyJhbGciOiJIUzI1NiJ9 ...
```

### User without admin role

There is one user without Admin Role. Use the previous call to get the token, but in `username` use `noadmin` and try to call anything. You'll get the response you deserve!

### Examples

These are examples of use the API with HTTPie. Use in all of these the parameter `--auth-type=jwt -v` 

* List of customers (**http://localhost:8080/customers**):

    ```bash
    http --auth-type=jwt -v [GET] http://localhost:8080/customers
    ```

* Get data for first customer (id 1) (**http://localhost:80807/customers/1**):

    ```bash
    http --auth-type=jwt -v [GET] http://localhost:8080/customers/1
    ```
    
* Create a new customer (**http://localhost:8080/customers** with **POST verb**):
    
    ```bash
    http --auth-type=jwt -v POST http://localhost:8080/customers firstName=Saulo lastName=Alvarado nid=99999999X
    ```
    
* Change/Edit data of a customer (**http://localhost:8080/customers** with **PUT verb**):

     ```bash
     http --auth-type=jwt -v PUT http://localhost:8080/customers id=1 firstName=Saulo lastName="Alvarado Mateos" nid=99999999X
     ```

* Delete first customer (**http://localhost:8080/customers/1** with **DELETE verb**):

    ```bash
    http --auth-type=jwt -v DELETE http://localhost:8080/customers/1
    ```

* To end the session:

    ```bash
    http [GET] http://localhost:8080/shutdown
    ```
