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
