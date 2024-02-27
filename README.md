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
* **vertx-logback-aleatsicsearch-appender** Vertx Logback ES Appender - indexing application log to ES and Axiom
* **reactiverse-contextual-logging** Using contextual logging (MDC) with logback
* **Slf4j API** `SimpleJsonResponseVerticle` uses Slf4J Logger API that supports eg placeholder replacing - whereas `LoggingAppender` uses just the vertx internal Logging API



## Build the app

Build the fatjar like this

```java
./gradlew shadowJar
```

## Run the app


To start multiple instances on different ports pass `server.port` property, default if not provided is `8080`

```java
java -jar -Daccess.location=/tmp -Dserver.port=8080 build/libs/shadow.jar
```

## Prometheus

Start dockerized prometheus by 

```java
cd docker-prometheus
docker-compose up -d
```

Adapt if needed its configuration in `config/prometheus.yml` 
 
```yaml
global: 
  scrape_interval: 15s 
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'vertx-playground-app'
    static_configs:
      - targets: ['host.docker.internal:8080', 'host.docker.internal:8081']
```

Open prometheus on [http://localhost:9090](http://localhost:9090)

## Execute sample request

This runs a simple GET request on the app that will produce some useful access log

```java
curl http://localhost:8080/test?requestId=test123 -v -H "Cookie: cookie1=cookie1Value; cookie2=cookie2Value"
```
