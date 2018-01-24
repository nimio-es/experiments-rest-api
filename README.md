# Second Stage of Technical Coding Interview

## The requirements 

What I have to do:

- Rest API for Customers. Each customer have to be defined with 
    - First name
    - Last name
    - National Document of Identity
    - Image (optional)
    - With each customer also store last purchases information

- Only admin roles can access the API. Restrict access using OAuth 2.

- Optionally, one or more cucumber tests to check one or more end-points


## The Fast Lane

Despite my wishes, I don't have too much time. The Internet is plagued with a lot of examples and projects that solve similar cases. Copying and pasting (and gutting and recomposing too) is a common practice and approach in the profession. Google `spring+boot+oauth2` and you'll get a lot of examples. Here my contribution to increase the Entropy of Universe. 

I really hope to find time (and feel like) to continue with the master branch and keep trying things.


### Inspiring resources

- The first approach was ~~gutting~~ adapting [Example Spring Boot REST API](https://github.com/gigsterous/gigy-example).
- [Spring Boot Security OAuth2 Example(Bcrypt Encoder)](http://www.devglan.com/spring-security/spring-boot-security-oauth2-example)
- [7 Steps to implement OAuth 2 in Spring Boot with Spring Security](https://jugbd.org/2017/09/19/implementing-oauth2-spring-boot-spring-security/)
- [Minimal implementation of Authorization Server, Resource Server and OAuth2 Client in Spring Boot with Spring Security and JWT](https://github.com/dynamind/spring-boot-security-oauth2-minimal)
- [Tutorial: Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Secure Spring REST API using OAuth2](http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/)


## Current version: What it does and what it does not do

This version does:

- CRUD for customers
- Customers include purchase information (one to many relation)
- There is an oauth url (auto-generated from Spring Boot) to get a token using password Grant Type and client-id + client-secret
- Unit tests and a pair of cucumber scenarios 
- Check role admin

This version does not (yet):

- Manage purchases 
- Poor version of purchase (It's necessary refer articles, for example?)
- Work with binary data (customer image)

Of those, the last one would be the real challenges. For the rest of those would be more of the same.


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

Run the API using:

```bash
mvn spring-boot:run
```

Now you can interact with it (some user preexist because the database is populated at startup).

To end the session you can break the execution with `CTRL+C`. 


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
http -v -f -a 'theam:secret' POST http://localhost:8080/oauth/token 'grant_type=password' 'username=noelia.capaz' 'password=password'
```

Copy the token (only the text) and set environment variable `JWT_AUTH_TOKEN` with previous value:


```bash
export JWT_AUTH_VALUE=eyJhbGciOiJIUzI1NiJ9 ...
```

### Different roles

In this example you can use two different users with distinct roles. 

- `noelia.capaz` with ADMIN role
- `pepito.currito` without ADMIN role (only USER role)

Both users has `password` as password. Isn't easy?

You can use `pepito.currito`to prove that only users with administrative permissions can access the API methods.

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
