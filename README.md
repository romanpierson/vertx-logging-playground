# Vertx Logging Playground

This project is used to demonstrate usage of vertx-web-accesslog and other log related components in a real vertx application.

# Features 

For now those features and functionalities are demoed

* **vertx-web-accesslog** `ConsoleAppender`
* **vertx-web-accesslog** `LoggingAppender` with Logback
* **vertx-web-accesslog** `ElasticSearchAppender` - indexing to ES and Axiom
* **vertx-web-accesslog** `PrefixableConsoleAppender` - example of custom Appender
* **vertx-web-accesslog** `MyCustomSecondDurationElement` - shows how you can replace an existing pattern with your custom implementation
* **vertx-logback-elasticsearch-appender** `Vertx Logback ES Appender` - indexing application log to ES and Axiom
* **reactiverse-contextual-logging** Using contextual logging (MDC) with logback
* **Slf4j API** `SimpleJsonResponseVerticle` uses Slf4J Logger API that supports eg placeholder replacing - whereas `LoggingAppender` uses just the vertx internal Logging API



## Build the app

Build the fatjar like this

```*.sh-session
./gradlew shadowJar
```

## Run the app


To start multiple instances on different ports pass `server.port` property, default if not provided is `8080`

```*.sh-session
java -jar -Daccess.location=/tmp -Dserver.port=8080 build/libs/shadow.jar
```

## Prometheus

Start dockerized prometheus by 

```*.sh-session
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

```*.sh-session
curl http://localhost:8080/test?requestId=test123 -v -H "Cookie: cookie1=cookie1Value; cookie2=cookie2Value"
```

## Execute random series of sample requests

As we have an endpoint that delays randomized based on a from to range passed in its url its easy to create some kind of randomized traffic

Eg using ab benchmark

```*.sh-session
ab -n 100 -c 10  -H 'x-ip: 81.202.235.150' http://localhost:8081/sleep/random/10/100?requestId=test123
```
