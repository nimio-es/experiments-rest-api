# Second Stage of Technical Coding Interview

## The requirements 

What I have to do:

- Rest API for Customers. Each customer have to be defined with 
    - First fileName
    - Last fileName
    - National Document of Identity
    - Image (optional)
    - With each customer also store last purchases information

- Only admin roles can access the API. Restrict access using OAuth 2.

- Optionally, one or more cucumber tests to check one or more end-points


## The Solution

A multimodule Maven project with a Server (API REST), a Common Library and a Command Line Client to interact with the server.

Spring Boot is the heart of the solution. Because develop with Spring Boot is really fast.


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
- Work with binary fileData (customer image)
- Command Line Client with most of the operations implemented.

This version does not (yet):

- Manage purchases 
- Poor version of purchase (It's necessary refer articles, for example?)

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
mvn clean install
```

Run the API using:

```bash
cd server
mvn spring-boot:run
```

Now you can interact with it (some user preexist because the database is populated at startup).

To end the session you can break the execution with `CTRL+C`. 


## Use the API

### API URIs

These are the resources offered by the API.
 
| URN | VERB | Purpose |
| --- | --- | --- |
| /customers  | GET | List all customers (if use query param "sorted", the result will be in order) |
| /customers/:id | GET | Gets fileData for the customer with :id |
| /customers | POST | Adds a new customer (when fileData is correct) |
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

### Access using CLI (recommended)

You cau use the Command Line Client to access the API. This option is recommended because the part of get OAuth Token is hidden to the user.

To use the client, open a new console terminal and go to client subforlder. Yoy can create an alias to the jar:

```bash
alias theam-cli="java -jar PATH_TO_TARGET_FOLDER/client-1.0.0-SNAPSHOT.jar"
``` 

After that you can call the command line using this alias. Use help command to know how to use:

```bash
theam-cli help
```

#### Authentication

All operation required the username and password information (noelia.capaz, password), but you can export environment variables to set those values and avoid to pass each time:

```bash
export THEAM_USERNAME=noelia.capaz
export THEAM_PASSWORD=password
```

If you use other user (`pepito.currito`) every operation will fail.

#### Examples

* Get customer list (json format):

    ```bash
    theam-cli customers
    ```
* Add a new customer:

    ```bash
    theam-cli customers add --first-fileName Oscar --last-fileName Rinconero --ndi 0000000
    ```

* Show an existent customer:

    ```bash
    theam-cli customers add --id 2 
    ```

* Set the image of a customer (the image has to exist):

    ```bash
    theam-cli customers image set --id 1 PATH_TO_EXISTENT_IMAGE 
    ```
    
* Get/Download the image of a customer and show in default application:

    ```bash
    theam-cli customers image get --id 1 --show PATH_TO_SAVE_IMAGE
    ```


### Access using HTTPie (or CURL)

Generally I prefer [HTTPie](https://httpie.org/) instead CURL. It's recommendable to install [httpie-jwt-auth](https://github.com/teracyhq/httpie-jwt-auth) plugin. In current version I use the JWT token simulation to protect the API.   

#### Before call

With each call to the API you need to include an oauth token. To get one, use the next command line operation:

```bash
http -v -f -a 'theam:secret' POST http://localhost:8080/oauth/token 'grant_type=password' 'username=noelia.capaz' 'password=password'
```

Copy the token (only the text) and set environment variable `JWT_AUTH_TOKEN` with previous value:


```bash
export JWT_AUTH_VALUE=eyJhbGciOiJIUzI1NiJ9 ...
```

#### Different roles

In this example you can use two different userData with distinct roles. 

- `noelia.capaz` with ADMIN role
- `pepito.currito` without ADMIN role (only USER role)

Both userData has `password` as password. Isn't easy?

You can use `pepito.currito`to prove that only userData with administrative permissions can access the API methods.

#### Examples

These are examples of use the API with HTTPie. Use in all of these the parameter `--auth-type=jwt -v` 

* List of customers (**http://localhost:8080/customers**):

    ```bash
    http --auth-type=jwt -v [GET] http://localhost:8080/customers
    ```

* Get fileData for first customer (id 1) (**http://localhost:80807/customers/1**):

    ```bash
    http --auth-type=jwt -v [GET] http://localhost:8080/customers/1
    ```
    
* Create a new customer (**http://localhost:8080/customers** with **POST verb**):
    
    ```bash
    http --auth-type=jwt -v POST http://localhost:8080/customers firstName=Saulo lastName=Alvarado nid=99999999X
    ```
    
* Change/Edit fileData of a customer (**http://localhost:8080/customers** with **PUT verb**):

     ```bash
     http --auth-type=jwt -v PUT http://localhost:8080/customers id=1 firstName=Saulo lastName="Alvarado Mateos" nid=99999999X
     ```

* Delete first customer (**http://localhost:8080/customers/1** with **DELETE verb**):

    ```bash
    http --auth-type=jwt -v DELETE http://localhost:8080/customers/1
    ```
