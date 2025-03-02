# Vertx Logging Playground

This project is used to demonstrate usage of vertx-web-accesslog and other log related components in a real vertx application.

# Features 

For now those features and functionalities are demoed

* **vertx-web-accesslog** ConsoleAppender
* **vertx-web-accesslog** LoggingAppender with Logback
* **vertx-web-accesslog** ElasticSearchAppender - indexing to ES
* **vertx-web-accesslog** ElasticSearchAppender - indexing to Axiom
* **vertx-web-accesslog** PrefixableConsoleAppender - example of custom Appender
* **vertx-web-accesslog** MyCustomSecondDurationElement - shows how you can replace an existing pattern with your custom implementation
* **vertx-logback-elasticcsearch-appender** Vertx Logback ES Appender - indexing application log to ES and Axiom
* **reactiverse-contextual-logging** Using contextual logging (MDC) with logback



## Build the app

Build the fatjar like this

```java
./gradlew shadowJar
```

## Run the app


```java
java -jar -Daccess.location=/tmp build/libs/shadow.jar
```


## Execute sample request

This runs a simple GET request on the app that will produce some useful access log

```java
curl http://localhost:8080/test?requestId=test123 -v -H "Cookie: cookie1=cookie1Value; cookie2=cookie2Value"
```
