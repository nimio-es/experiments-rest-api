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

Despite my wishes, I don't have too much time. The Internet is plagued with a lot of examples and projects that solve similar cases. Copying and pasting (and gutting and recomposing too) is a common practice and approach in the profession. Here my contribution to increase the Entropy of Universe.

I hope to find time (and feel like) to continue with the master branch and keep trying things.

## The **OLD** Plan Was... (_Unreal in this Reality_)

Some ideas of how to be the process to build the solution.

| Milestone | Explanation | Status |
| --- | --- | :---: |
| Tech immersion | Start playing with [Spark Framework](http://sparkjava.com/) and with [Pac4j](http://www.pac4j.org/). Go beyond the Spring Framework. Spring provides a sort of automatic mechanisms that facilitate, and do for you, almost everything. Try new things. The result will be a lot of dirty code with most of things hardcoded and storage using memory. A basic REST API with token authentication. And [Kotlin](https://kotlinlang.org/), of course! **Why Spark?** Because it's **light**, it's **fast** and it's **cool**. :sunglasses: | In progress | 
| Model & Code improvements | Continue learning about technological decisions, but oriented to establish a common vocabulary inside the API and outside it. Some unit tests also and some code testing coverage. | Thinking about |
| Persistence & Functional/Integration Tests | Use any sort of storage to persist the model. May be MongoDB, may be PostgresQL, may be plain file system. Probably the use of Docker (and Compose) to start a repeatable scenario. And... **Cucumber** appears in action! (Really I'll use [the JVM implementation](https://cucumber.io/docs/reference/jvm)) | Pending... |
| OAuth 2 | Indirect flow. A little web application to call GitHub, Facebook or whatever to authenticate a user and to generate a token to use when call. No more fake tokens, please. | Pending ... |
| The Console Client | A console client to interact with the API. Things like `theam customers list` in console to get the list of all customers | May be... |
| The Web Client | Extend/Continue with the web application started with OAuth2 to show and interact with the model | May be... |
| The Phone Client | Probably not, but sounds good. However, this time no JVM solution. I'll use F# and Xamarin.Forms to build a solution able to work in any device | Probably not... |
| The System | Nginx, API Gateway, Microservices... And all those cute things which the technicians dream of | Probably not... | 


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

Generally I prefer [HTTPie](https://httpie.org/) instead CURL. It's recommendable to install [httpie-jwt-auth](https://github.com/teracyhq/httpie-jwt-auth) plugin. In current version I use the JWT token simulation to protect the API.   

### Before call

Get the simulated token using `http http://localhost:8080/admintoken`. Copy the token (only the text) and set environment variable `JWT_AUTH_TOKEN` with previous value:

```bash
export JWT_AUTH_VALUE=eyJhbGciOiJIUzI1NiJ9 ...
```

### The anti-token

Yeah, you could call `/nobodytoken` to generate a token without admin role. Only if you want to see the api respond with Forbidden

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
