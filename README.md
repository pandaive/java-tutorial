This is the very basic Java + Gradle + Spring boot tutorial.
Follow these steps to create your first Spring boot API using Gradle and Java 8.

<h2> Prerequisites </h2>

* java 8 (`sudo apt-get install openjdk-8-jdk`)
* gradle (`sudo apt-get install gradle`)
* if you use eclipse/intellij, install gradle support and lombok support https://projectlombok.org/
* git clone this repository

<h2> Gradle configuration </h2>

For details about Gradle tool, refer to https://gradle.org/

In this repository, you can find basic gradle project structure:

```
├── src
│   ├── main
│   │   ├── java
│   │       ├── tutorial
│   ├── test
│   │   ├── java
│   │       ├── tutorial
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
```

Source files are split into `main` - java classes and `test` - unit tests for java classes. By convention you keep classes and their tests in the same directory schema so, having class Library and test class LibraryTest, you would keep them like this:

```
├── src
│   ├── main
│   │   ├── java
│   │       ├── tutorial
│   │           ├── Library.java
│   ├── test
│   │   ├── java
│   │       ├── tutorial
│   │           ├── LibraryTest.java
```

Check out the `build.gradle` file. It contains project plugins, dependencies, repositories, build scripts etc. Now it's initialized with basic java plugin, test library dependency and jcenter() repository.

------------------------

<h3> For solutions to each steps, checkout step-x branches. </h3>

<h2> Step 1: Start gradle application </h2>

1. Checkout that project is built correctly with gradle using gradle build tool:

```
./gradlew build
```

You should see BUILD SUCCESSFUL output.

To start an application, you do

```
./gradlew run
```

Right now the build will fail, because there is no executable file.

Let's create application starting point: class Application.java in tutorial package (src/main/java/tutorial/Application.java):

```java
package tutorial;

public class Application {

	public static void main(String[] args) {
		System.out.println("Hello, world!");
	}

}
```

In each class you must define package and class. Main method is a starting point for your application.

Try to run `./gradlew build`. Build shoud be successful. Now try to run it with `./gradlew run`. Build will fail, because you haven't specify that Application.java is your starting point.

Let's go to build.gradle and add necessary things:

```java
apply plugin: 'java-library'
apply plugin: 'application'

mainClassName = entrypoint

sourceCompatibility = JavaVersion.VERSION_1_8
targetCompatibility = JavaVersion.VERSION_1_8
```

The `application` plugin facilitates creating an executable JVM application. `mainClassName` defines the main class. You also specify compatibility of your application. `mainClassName` is set to entrypoint, so we want to keep such stuff in a gradle properties file to keep everything clean.

Create `gradle.properties` file in a project root directory with entrypoint declaration:

```java
entrypoint=tutorial.Application
```

Now do `./gradlew build` and `./gradlew run` again. Now you should see Hello World message!

From now on when "run application" is said, it means executing those both commands if not stated otherwise.

<h2> Step 2: create Spring Boot application </h2>

We want to create Spring Boot API. For details about Spring Boot, refer to https://spring.io/projects/spring-boot, espacially this tutorial: https://spring.io/guides/gs/spring-boot/

We will need to add Spring Boot plugin, dependency and a buildscript first:

```java
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.springframework.boot:spring-boot-gradle-plugin:" + frameworkVersion
    }
}

...
apply plugin: 'application'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

...

dependencies {
	compile "org.springframework.boot:spring-boot-starter"
	compile "org.springframework.boot:spring-boot-starter-web"
	compile "org.springframework.boot:spring-boot-starter-test"
...
}

...
```
We have a new variable frameworkVersion here, let's add it in gradle.properties:

```java
entrypoint=tutorial.Application
frameworkVersion=2.0.5.RELEASE
```

Now build your project and check that dependencies are downloaded and build is successful.

Let's specify that our application is a spring boot application by adding @SpringBootApplication annotation to our main class and call `run` method inside main class. This will start our spring boot app.

```java
package tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

You will see your application started with Spring logo, a lot of logs and finally a log indicating that application started correctly:

```
2019-03-12 09:37:51.444  INFO 17860 --- [main] tutorial.Application: Started Application in 4.125 seconds (JVM running for 4.572)
```

To terminate the application press Ctrl+C.

<h2> Step 3: Create first API endpoints </h2>

Having Spring Boot running our API, let's start defining endpoints. Let's create a new directory `web` in our tutorial package, which will hold our API endpoints declarations. Let's create Query class and a method which will return Hello World to the API call.

```java
package tutorial.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myapi")
public class Query {

	@GetMapping(path = "/hello")
	public String getHello() {
		return "Hello, world!";
	}

}
```

We annotate it with `@RestController` to indicate that this class containts Rest endpoints and we specify path mappings, one to whole class - automatically prefixed to each endpoint path and one to specific endpoint. Now build and run your project. By default, your API will be listening on port 8080. Use postman to GET `http://localhost:8080/myapi/hello` or just type the address in your browser. You should see a Hello World response! Also checkout the logs in your terminal to see that spring handled this request.

Take a time to think about unit test to your endpoint. Create a `QueryTest.java` in `src/test/java/tutorial/web` and create a test using MockMvc to mock your request.

```java
package tutorial.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QueryTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHello() throws Exception {
		 mvc.perform(MockMvcRequestBuilders.get("/myapi/hello").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello, world!"));
	}

}
```

Now build your application again. You should see additional test task from gradle. If tests are passing, build will be succesfull, otherwise gradle will generate you a report in build/reports directory. Change expected message to see test failing and the report.

<h2> Step 4: Path params, request params, service classes </h2>

First of all, we want to move the functionality of request handling from `web/Query` class to keep only endpoint declaration there. It will come useful when we do more complicated endpoints. Create `tutorial/service/` directory with `QueryService.java` class inside. Annotate it as a `@Component` so we will be able to inject it in `Query` class and use it to handle the request.

```java
package tutorial.service;

import org.springframework.stereotype.Component;

@Component
public class QueryService {

	public String getHello() {
		return "Hello, World";
	}

}
```

Now use this class in `Query.java`.

```java
...

import org.springframework.beans.factory.annotation.Autowired;
import tutorial.service.QueryService;

public class Query {

	@Autowired
	QueryService queryService;

	@GetMapping(path = "/hello")
	public String getHello() {
		return queryService.getHello();
	}
```

Check that your tests are still passing! If not, check the fail reason and correct the issue :)

Create also a test for your service class in src/test/java/tutorial/service, call it QueryServiceTest.java. This one will be simpler, as we don't need to mock api request:

```java
package tutorial.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryServiceTest {

	@Autowired
	QueryService queryService;

	@Test
	public void getHelloTest() {
		assertEquals("Hello, World!", queryService.getHello());
	}

}
```

Notice that you again autowire `queryService` object to use it.

Now let's handle the path param and say hello to you. You need to add it in a path declaration and also pass it to a method as a param. Spring will handle that for you, just use `@PathVariable` annotation.

```java
import org.springframework.web.bind.annotation.PathVariable;

...

	@GetMapping(path = "/hello/{name}")
	public String getHello(@PathVariable("name") String name) {
		return queryService.getHello(name);
	}
```

Adjust `getHello()` method in QueryService class to handle the param:

```java
	public String getHello(String name) {
		return "Hello " + name + "!";
	}
```

If you build your application now, tests will fail. You need to also adjust it. Just add a param in request path: `MockMvcRequestBuilders.get("/myapi/hello/world")`. In QueryServiceTest add a param to test if this method will work correctly. Check that your application is built correctly. You can also run it and check it in Postman/browser with a new path.

If you want to add a request parameter (/myapi/hello?name=world) you don't need to declare it in path mapping, just add a parameter in your method like:

```java
	@GetMapping(path = "/hello")
	public String getHello(@RequestParam(value="name", required=true) String name) {
		return queryService.getHello(name);
	}
```

You can try it out by yourself.

<h2> Step 5: POST endpoint, request body </h5>

Finally let's create a POST endpoint (put/delete will work similarly).

Let's create another class in `web/` directory, `Command.java`. Annotate it with `@RestController` and add a method to handle the post request to your API.

Create `CommandService.java` in service directory, annotate it as Component and autowire it into `Command.java`. The same way as with `Query` classes.

Define path mapping. Path mapping of command can be the same as of query (`@RequestMapping("/myapi")`).

Add a test classes `CommandTest.java` and `CommandServiceTest.java` in respective directories.

To handle POST request we will need to define model class, so the spring will get json request body and inject it in an object of this class.

Let's build an endpoint to calculate the bill price. We will need to provide bill information: product quantity, price and vat rate :wink:

Create `tutorial/model` directory. Create a `Bill.java` class and declare 3 fields:

```java
package tutorial.model;

public class Bill {

	private int quantity;
	private double price;
	private double vatRate;

}
```

In Java you declare class fields as private and you access them by getter and setters from outside. Always. But we don't want to write getters and setters for each fields and that's when lombok library comes handy. It comes with several handy annotations that will preprocess the class and do the thing for us.

Let's declare it as a dependency in our project in build.gradle:

```
dependencies {
	compile "org.springframework.boot:spring-boot-starter"
	compile "org.springframework.boot:spring-boot-starter-web"
	compile "org.springframework.boot:spring-boot-starter-test"

	compile 'org.projectlombok:lombok:1.16.10'

	...
}
```

If you use IDE, you may need to refresh your gradle project to be able to use those annotations. Otherwise, just do `./gradlew build` to download the libraries. You can checkout lombok annotations here: https://projectlombok.org/features/all

We want to use `@Data` annotation, to generate getters and setters. Add it for the class, and you can assume that you have getters and setters for all of the fields.

```java
import lombok.Data;

@Data
public class Bill {
	...
}
```

Now let's create a POST endpoint in `Command.java` which will receive json request body with fields as declared in Bill class.

```java
	@PostMapping(path = "/price", consumes = "application/json")
	public double calculatePrice(@RequestBody Bill request) {
		return (request.getPrice() + request.getPrice() * request.getVatRate()) * request.getQuantity();
	}

```

We use the PostMapping annotation class in which we specify param and request format (json). We accept request as a Bill param, annotated by @RequestBody. Easy.

Let's also create a unit test for this endpoint in src/test/java/tutorial/web/CommandTest.java:

```java

	@Test
	public void calculatePriceTest() throws Exception {
		Bill bill = new Bill(5, 2, 0.2);
		String expectedPrice = "12.0";
		ObjectMapper mapper = new ObjectMapper();
		String billJson = mapper.writeValueAsString(bill);

		 mvc.perform(MockMvcRequestBuilders.post("/myapi/price")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(billJson)
				 .accept(MediaType.APPLICATION_JSON))
		 .andExpect(status().isOk())
		 .andExpect(content().string(expectedPrice));
	}

```

We use ObjectMapper from jackson library (import com.fasterxml.jackson.databind.ObjectMapper;) to map our class into json string. In this method you create a new Bill object with a constructor will all arguments which is not declared in the class. You can either add it manually:

```java
	public Bill(int quantity, double price, double vatRate) {
		this.quantity = quantity;
		this.price = price;
		this.vatRate = vatRate;
	}
```

or use lombok library again and simply add @AllArgsConstructor annotation to your class, like @Data. Add also @NoArgsConstructor annotation, required by spring to use it as a request body model.


Again, we don't want to have functionality in Command.java, just endpoint declaration. Create a method in CommandService.java to calculate the bill, with Bill object as param. In calculatePrice() method pass a request to it and return the result. Add the unit test for this class as well. (Note: assertEquals for double params requires 3rd argument, delta for floating point numbers comparison. Just add 0 as a delta param: `assertEquals(12.0, commandService.getPrice(new Bill(5, 2, 0.2)), 0);`)

You can test your endpoint in Postman by sending POST request with following request body:

```java
{
	"quantity": 5,
	"price": 2,
	"vatRate": 0.2
}
```

or with curl:

```
curl -X POST \
  http://localhost:8080/myapi/price \
  -H 'Content-Type: application/json' \
  -d '{
	"quantity": 5,
	"price": 2,
	"vatRate": 0.2
}'
```

<h2> Step 6: Custom errors and error handling </h2>

Sometimes you may want to have custom errors or custom error handling. Now, if you put a string in one of the fields in your POST request body, you will see an error like this one:

```
{
    "timestamp": "2019-03-12T12:56:50.768+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "JSON parse error: Cannot deserialize value of type `int` from String \"blabla\": not a valid Integer value; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `int` from String \"blabla\": not a valid Integer value\n at [Source: (PushbackInputStream); line: 2, column: 14] (through reference chain: tutorial.model.Bill[\"quantity\"])",
    "path": "/myapi/price"
}
```

But you may not want to expose your implementation or you may want to send back specific error message/status code. In this case you can create an error handler. Spring Boot comes with @RestControllerAdvice annotation in which you can write your own exception handlers. If you checkout application logs after getting error as above, you will find information about HttpMessageNotReadableException, so this is the one that you want to overwrite.

Create directory `/tutorial/exception/` and Handler.java class inside like following:

```java
package tutorial.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class Handler {

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public void handle(HttpMessageNotReadableException e, HttpServletResponse response) throws IOException {
        String message = "Invalid value given in request body. " + convertHttpNotReadableExceptionMessage(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), message);
    }

	private String convertHttpNotReadableExceptionMessage(String message) {
        String firstLine = message.substring(message.indexOf("type"), message.indexOf("\n"));
        String[] s = firstLine.split(" ");
        String[] fields = message.split("\"");
        String fieldName = fields[fields.length-2];
        String requiredValue = s[s.length-2];
        String givenValueType = s[3];
        String givenValue = fields[1];

        return "Field " + fieldName + " must be of type " + requiredValue + ". "
                + "Value of type " + givenValueType + " provided: " + givenValue + ".";
    }

}
```

You create a method handle() and specify which exception to handle in @ExceptionHandler annotation with specific exception as value and get exception itself and response object in method parameters. Here I we have added very simple message parser because in this case, we want a nicer message. We keep error code 400.

You may also want to create your own exception type. Using our POST price endpoint, we may want to force users to use maximum quantity of 10 for some reasons. Let's create our own exception in `/tutorial/exception/` directory and name it ex. MaximumQuantityExceededException.java

```java
package tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaximumQuantityExceededException extends RuntimeException {

	public MaximumQuantityExceededException(int quantity) {
		super("Quantity " + quantity + " exceeds maximum quantity allowed: 10");
	}

}
```

Then we can use this exception when validating incoming request (CommandService.java):

```java
public double getPrice(Bill bill) {
		if (bill.getQuantity() > 10) {
			throw new MaximumQuantityExceededException(bill.getQuantity());
		}
		return (bill.getPrice() + bill.getPrice() * bill.getVatRate()) * bill.getQuantity();
	}
```

Now try to send request with quantity higher than 10.

<h2> Step 7: logs </h2>

No need for introduction what logs are for. Lombok comes with a @Slf4j class annotation which gives you static final `log` field of type Logger which you can use to add custom logs to your API within annotated class.

For usage, you simply import Slf4j and add @Slf4j annotation like here:

```java
...

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/myapi")
@Slf4j
public class Command {

...
```

and then you can use log.info(msg), log.error(msg), log.warn(msg) etc. Checkout the Slf4j.Logger documentation here: https://www.slf4j.org/api/org/slf4j/Logger.html for details and all available methods.

Let's add a log to see incoming request in our POST endpoint with request body in it (using method info(String format, Object arg)):

```java
@RequestMapping("/myapi")
@RestController
@Slf4j
public class Command {
	
	@Autowired
	CommandService commandService;
	
	@PostMapping(path = "/price", consumes = "application/json")
	public double calculatePrice(@RequestBody Bill request) {
		log.info("[Command] Received new calculatePrice request. Request body: {}", request);
		return commandService.getPrice(request);
	}
}
```

Start the API, shoot a POST request to this endpoint and check the logs in terminal for log like this:

```
2019-03-14 15:02:44.067  INFO 28684 --- [nio-8080-exec-1] tutorial.web.Command : [Command] Received new calculatePrice request. Request body: Bill(quantity=5, price=2.0, vatRate=0.2)
```

<h2> Step 8: Swagger </h2>

Swagger is a useful tool to generate documentation based on a code itself. Read more here: https://swagger.io/ and http://springfox.github.io/springfox/

First we add a springfox-swagger2 dependency in our build.gradle file:

```
...

    compile 'org.projectlombok:lombok:1.16.10'

    compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.6.1'

    // Use JUnit test framework
    testCompile 'junit:junit:4.12'
    
...
```

Later we annotate parts of our API with descriptions. Checkout available annotations here: https://github.com/swagger-api/swagger-core/wiki/annotations#quick-annotation-overview.

Annotate classes with endpoints with @Api to indicate that they are swagger resources and annotate HTTP methods with @ApiOperation and @ApiResponses annotations:

```java
...

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/myapi")
public class Query {
	
	@Autowired
	QueryService queryService;
	
	@ApiOperation(value = "Get name", notes = "Get yourself a hello")
	@GetMapping(path = "/hello/{name}")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Success", response = String.class),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 500, message = "Failure")})
	public String getHello(@PathVariable String name) {
		return queryService.getHello(name);
	}

}
```

```java
...

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RequestMapping("/myapi")
@RestController
@Slf4j
public class Command {
	
	@Autowired
	CommandService commandService;
	
	@ApiOperation(value = "Calculate price", notes = "Send your bill, get the price")
	@PostMapping(path = "/price", consumes = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Success", response = Double.class),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 500, message = "Failure")})
	public double calculatePrice(@RequestBody Bill request) {
		log.info("[Command] Received new calculatePrice request. Request body: {}", request);
		return commandService.getPrice(request);
	}
}
```

Then also annotate a model used in a POST request to describe it in documentation as @ApiModel and its fields as @ApiModelProperty:

```java
...

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Bill", description = "Data about the bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

	@ApiModelProperty(dataType = "Integer", value = "Quantity of a product on a bill", required = true, example = "5")
	private int quantity;
	
	@ApiModelProperty(dataType = "Double", value = "Price of a product", required = true, example = "15.99")
	private double price;
	
	@ApiModelProperty(dataType = "Double", value = "VAT rate to be used to calculate the bill", required = false, example = "0.23")
	private double vatRate;
	
}
```

Now you need to enable swagger api-doc path by annotating main API class:

```java
...

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

Now run your API, you can access JSON representation of an API at `http://localhost:8080/v2/api-docs`.

There is another springfox-swagger tool that will create a nice visual documentation with testing tools, to use it, just add another dependency and rebuild and run your API.

```
	compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.6.1'
	compile group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.6.1'
```

You can access generated documentation and try it out here: `http://localhost:8080/swagger-ui.html`