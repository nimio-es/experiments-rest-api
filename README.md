# Experiments

See master branch README.md for understanding of the scope of this repository.

## The "Current" Solution

Small or no functional difference with the base version made with Maven, but:

* [Gradle](https://gradle.org/) as build tool.
* [Arrow Library](https://www.47deg.com/blog/announcing-arrow-for-kotlin/), Functional Programming for Kotlin.
    * At least for doing [arguments validation](http://arrow-kt.io/docs/datatypes/validated/) 
* Transform all code to Kotlin (Spring Boot) and remove any Java class.
* [Vagrant](https://www.vagrantup.com/)

## How to try this pile of...

### Prerequisites
 
First of all, you need Java 8, at least. Gradle for building is required, but you can use the gradlew script to do all the job when Gradle isn't in your system pre-installed. Of course, a Git client.

Or you can use [Vagrant](https://www.vagrantup.com/) for isolating the development environment. Up to you! 

But above all else, you need a lot of patience and desire to try it.

### Build and start up

1. Clone (download) this repository: 
    ```bash
    git clone https://github.com/saulo-alvarado/experiments-rest-api.git
    ```
1. Path to the folder
1. In case you decided to use Vagrant, you need to start it and connect with:
    1. `vagrant up` to start the environment
    1. `vagrant ssh` to connect to the Virtual Machine where the environment is
    1. `cd /vagrant`, where the shared folder is in the guest machine
    1. where in the next steps you see `gradlew` prefix it with `./`, then use `./gradlew`
1. Build with Gradle:
    ```bash
    gradlew clean 
    gradlew build
    gradlew fatJar
    ```
    
    `fatJar` is a custom task defined into client for building a independent jar with all dependencies into it.
    
1. Start REST api in one terminal. In root directory you'll found `start-server.bat` (Windows) and `start-server.sh` (Linux). Use the one required for your environment.
1. Only for Linux users, create an alias to the client JAR:
        ```bash
        alias nivi='java -jar PATH-TO-PROJECT/client/build/libs/client-all-*.jar'
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
