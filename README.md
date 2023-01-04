# Vertx Logging Playground

This project is used to demonstrate usage of vertx-web-accesslog and other log related components in a real vertx application.

# Features 

For now those features and functionalities are demoed

* **vertx-web-accesslog** ConsoleAppender
* **vertx-web-accesslog** LoggingAppender with Logback
* **vertx-web-accesslog** ElasticSearchAppender (still without ES setup - to come)
* **vertx-web-accesslog** PrefixableConsoleAppender - example of custom Appender
* **reactiverse-contextual-logging** Using contextual logging (MDC) with logback



## How to run the app

As gradle vertx plugin is used you can just start the application with 

```java
gradle vertxRun
```

or debug it with 

```java
gradle vertxDebug
```

## Execute sample request

This runs a simple GET request on the app that will produce some useful access log

```java
curl http://localhost:8080/test?requestId=test123 -v -H "Cookie: cookie1=cookie1Value; cookie2=cookie2Value"
```
