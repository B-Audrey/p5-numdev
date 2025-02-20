# Yoga App !

# Getting Started
This project has been made for testing. It is not yet ready for production.

## Requirements

Ton run this project, you need to have the following tools installed on your computer :

* Java 11 : https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
* Maven : https://maven.apache.org/download.cgi
* Docker : https://www.docker.com/get-started/

* Sdkman (is optionnal if you already have the Java 11 version) : https://sdkman.io/install
  You can use sdkman to manage your java version, you can install java 21.0.4-oracle and use it by the following
  command :

``` bash
sdk install 11.0.14.1-jbr
sdk use java 11.0.14.1-jbr
``` 

you can also find a file named .sdkmanrc to easily change version between projects by using the following command :

``` bash
sdk env
```

## INIT DB
## DB

command to init a new db in docker :

``` bash
docker run --name test -e MYSQL_USER=user -e MYSQL_PASSWORD=123456 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
```

to start db :

``` bash
docker start test
```

to stop db :

``` bash
docker stop test
```

to remove db :

``` bash
docker rm test
```

## Running the application

To run the application, you first need to install then run, you can use the following command :

``` bash
mvn clean install
```

Then you can run the application by using the following command :

``` bash
mvn spring-boot:run
```

## Running the tests

To run the tests, you can use the following command :

``` bash
mvn verify
```

This command will run the tests and generate TWO report in the target folder.
You will find the report in the
target/site/jacoco/index.html file for unit testing
and in the 
target/integration/index.html file for integration testing.