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
│   ├── test
│   │   ├── java
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
│   │       ├── Library.java
│   ├── test
│   │   ├── java
│   │       ├── LibraryTest.java
```

Check out the build.gradle file. It contains project plugins, dependencies, repositories, build scripts etc. Now it's initialized with basic java plugin, test library dependency and jcenter() repository.