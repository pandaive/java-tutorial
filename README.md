This is the very basic Java + Gradle + Spring boot tutorial.
Follow these steps to create your first Spring boot API using Gradle and Java 8.

<h2> Prerequisites </h2>

* java 8 (sudo apt-get install openjdk-8-jdk)
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

Source files are split into main - java classes and test - unit tests for java classes. By convention you keep classes and their tests in the same directory schema so, having class Library and test class LibraryTest, you would keep them like this:

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

Check out the build.gradle file. It contains project plugins, dependencies, repositories, build scripts etc. Now it's initialized with basic java plugin, test library dependency and jcenter() repository.

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
		System.out.println("Hello world!");
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

The Application plugin facilitates creating an executable JVM application.  MainClassName defines the main class. You also specify compatibility of your application. mainClassName is set to entrypoint, so we want to keep such stuff in a gradle properties file to keep everything clean.

Create `gradle.properties` file in a project root directory with entrypoint declaration:

```java
entrypoint=tutorial.Application
```

Now do `./gradlew build` and `./gradlew run` again. Now you should see Hello World message!

Further when "run application" stated, it means executing those both commands if not stated otherwise.

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
		return "Hello world!";
	}

}
```

We annotate it with @RestController to indicate that this class containts Rest endpoints and we specify path mappings, one to whole class - automatically prefixed to each endpoint path and one to specific endpoint. Now build and run your project. By default, your API will be listening on port 8080. Use postman to GET http://localhost:8080/myapi/hello or just type the address in your browser. You should see a Hello World response! Also checkout the logs in your terminal to see that spring handled this request.

Take a time to think about unit test to your endpoint. Create a QueryTest.java in src/test/java/tutorial/web and create a test using MockMvc to mock your request.

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
         	.andExpect(content().string("Hello world!"));
	}

}
```

Now build your application again. You should see additional test task from gradle. If tests are passing, build will be succesfull, otherwise gradle will generate you a report in build/reports directory. Change expected message to see test failing and the report.

<h2> Step 4: Path params, POST method, request body, service classes </h2>

First of all, we want to move the functionality of request handling from web/Query class to keep only endpoint declaration there. Create tutorial/service directory and create QueryService.java class inside. Annotate it as a @Component so we will be able to inject it in Query class and use it to handle the request.

```java
package tutorial.service;

import org.springframework.stereotype.Component;

@Component
public class QueryService {
	
	public String getHello() {
		return "Hello World";
	}
	
}
```

Now use this class in Query.java.

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

Now let's handle the path param and say hello to you.