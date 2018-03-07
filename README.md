# Experiments

## Some preliminary clarifications (prolegomena)

This project started as a technical test proposed by [theam.io](http://theam.io/en) in order to collaborate with them. I know some of them in person and I know that they are spectacular professionals, very talented and very motivated people. Thank you very much for the possibility and for the time you dedicated to assist me and review the test.

Regardless of the final result, it has gotten me interested in doing things differently than what I've doing in recent years. And try new things too. For some time I'm very interested in Functional Programming but I haven't spent time in putting it into practice, for example.

In that sense, I hope that this will be my experimentation project to prove things. To build and to destroy a moment later. But I will try to maintain the original sense of the proposed problem.

The objective of all this is to mature and put in practice ideas to resuscitate my other project: NOVA (a.k.a. *nimiogcs*). I confess that is the apple of my eyes ;-)


## The original requirements 

What I had to do:

- Rest API for Customers. Each customer have to be defined with 
    - First Name
    - Last Name
    - National Document of Identity
    - Image (optional)
    - With each customer also store last purchases information

- Only admin roles can access the API. Restrict access using OAuth 2.

- Optionally, but really optional, one or more cucumber tests to check one or more end-points.


## Iterations

The main idea is to maintain only one iteration in master/default branch (no branches) and, at the end of each one, create a tag and start a new iteration. 

- Iteration 0 (or The Panic Iteration): The first attempt. JVM World using Spark Framework. Tag: [iteration-0_jvm-using-spark-framework](https://github.com/saulo-alvarado/experiments-rest-api/tree/iteration-0_jvm-using-spark-framework).

    The panic one because it is the iteration with which I started just when they told me that I was going to the second phase of the selection process. :scream:

## The "Current" Solution: Iteration 1

At this moment the solution consists of a multi-module Maven project with a Server (REST API), a Common Library and a Command Line Client to interact with the server. The server part was coded in Java and the rest was coded using Kotlin.

What I try to learn here:

* Spring Boot: every Java developer should understand the basis of Spring Framework
* OAuth 2: Yeah, the standard security protocol for REST APIs
* Kotlin: Improve the use of this language
* ~~Cucumber/Clojure: At least a bit taste of JVM flavour using Clojure (Clojure is a great unknown to me)~~  Out of focus at the moment 'cause I'll change iteration.
* RESTful APIs: Not so unknown, but I seek to achieve purity in the approach

## How to try this pile of...

### Prerequisites
 
First of all, you need Java 8, at least. Maven too. Of course, a Git client. But above all else, patience and desire to try it.

### Build and start up

1. Clone (download) this repository: 
    ```bash
    git clone https://github.com/saulo-alvarado/theam.io-api-rest-stage-2.git
    ```
1. Path to the folder
1. Build with Maven:
    ```bash
    mvn clean package
    ```
1. Start REST api in one terminal. In root directory you'll found `start-server.bat` (Windows) and `start-server.sh` (Linux). Use the one required for your environment.
1. Only for Linux users, create an alias to the client JAR:
        ```bash
        alias nivi='java -jar PATH-TO-PROJECT/client/target/client-*-jar-with-dependencies.jar'
        ```
1. Optionally, you can populate the database with `init-data.bat` (Windows) or `init-data.sh` (Linux) and skip some of the next steps.
1. If you chose not to fill in automatically, you will have to do:
    1. Register an administrator user. In the beginning there is only one owner user and for everything else you need an administrator user.
        ```bash
        nivi users create --new-username=nieves --new-password=guapa --as-admin --username=owner --password=password
        ```
1. Set the environment variables so that you don't have to pass the user and the password constantly. In Windows:
    ```bash
    SET NIMIO_USERNAME=nieves
    SET NIMIO_PASSWORD=guapa
    ```
    In Linux:
    ```bash
    export NIMIO_USERNAME=nieves
    export NIMIO_PASSWORD=guapa
    ```
    Obviously put the values that you used if you had done it manually.
1. Request to the CLI for help and play with it. In the `init-data.bat` you can see different uses of the CLI. Some examples:
    ```bash
    nivi customers add --first-name Lourdes --last-name Carmona --ndi 123456789X
    nivi customers add --first-name Pablo --last-name Motos --ndi 987654321Y
    nivi customers list
    nivi products create --reference "XAA0913" --name "Gominolas" --common-price 0.01
    nivi products create --reference "PIASS12" --name "Lápices" --common-price 9.81
    nivi purchases add --customer-id 1 --product-id 1 --num-of-items 20 --unit-price=0.008
    nivi purchases add --customer-id 2 --product-id 1 --num-of-items 11 --unit-price=0.012
    nivi purchases list --customer-id 1
    nivi purchases list --product-id 1
    ```

### The modules

The solution consists of three modules:

#### Common

Used by the other two modules, in essence *it's a silly and unnecessary* module, but it's here to facilitate the typed construction of the client one and to simulate that the REST Api is a kind of Gateway where the data is not direct, but some composition of queries to various "services". It gives a lot of unnecessary work of data transformation, but on the other hand it allows to code the client fast.

Kotlin and not much else to tell.

#### Server

The Spring Boot one. It's the REST Api bases on an H2 database that doesn't persist anything. You'll lose all every time you stop the service.

Pure Java and a lot of magic done by Spring.

#### Client

For me, the most interesting one. A lot of commands to create, show and more data in your console without to fight with URLs, parameters and authentication.

Kotlin and some experiments with absurd DSLs.   

### The Http Rest Api

Of course you can access each of the endpoints offered by the API.

I highly recommend installing [HTTPie](https://httpie.org/) and the [httpie-jwt-auth](https://github.com/teracyhq/httpie-jwt-auth) plugin.

You will also need to create an administrator user.

1. First of all you need to get the token of the owner user:
    ```bash
    http -v -f -a 'nimio:nivi-is-beautiful' POST http://localhost:8080/oauth/token 'grant_type=password' 'username=owner' 'password=password'
    ```
1. Export the token value `SET JWT_AUTH_TOKEN=eyJhbGciOiJIUzI1NiJ9...` or `export JWT_AUTH_TOKEN=eyJhbGciOiJIUzI1NiJ9`
1. Create a new user:
    ```
    http --auth-type=jwt -v POST http://localhost:8080/users?username=nieves&password=guapa&asAdmin=true
    ```
1. Ask for OAuth token of the new user (like the first step but using the new user credentials) and change JWT_AUTH_TOKEN environment variable.
1. Play with rest of the endpoints:
    ```bash
    http --auth-type=jwt -v POST http://localhost:8080/customers firstName=Saulo lastName=Alvarado nid=99999999X
    http --auth-type=jwt -v GET http://localhost:8080/customers/1
    http --auth-type=jwt -v DELETE http://localhost:8080/customers/1
    ...
    ``` 

#### API URIs

These are the resources offered by the API.
 
| URN | VERB | Purpose |
| --- | --- | --- |
| /users | GET | List all users |
| /users?username=&password=&asAdmin=true/false | POST | Adds a new user | 
| /customers  | GET | List all customers (if use query param "sorted", the result will be in order) |
| /customers/:id | GET | Gets fileData for the customer with :id |
| /customers | POST | Adds a new customer (when fileData is correct) |
| /customers/:id | DELETE | Deletes the customer with the :id |
| /customers/:id/image | GET | Gets the image associated with the customer |
| /customers/:id/image | POST | Store a new (or substitute) the image associated with the customer |
| /images | GET | List of all registered images |
| /images/:id | GET | Gets/Download one image |
| /products | GET | List of all registered products |
| /products/:id | GET | Get the info of one product |
| /products | POST | Adds a new product |
| /purchases/ofCustomer/:customerId | GET | List of the purchases of one customer |
| /purchases/ofProduct/:productId | GET | List of the purchases in which a product has been sold |
| /purchases/ofCustomer/:customerId/ofProduct/:productId | GET | List of the purchases of one customer in which a product has been sold |
| /purchases | POST | Adds a new purchase |
