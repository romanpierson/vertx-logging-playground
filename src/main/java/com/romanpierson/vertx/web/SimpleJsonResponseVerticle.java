package com.romanpierson.vertx.web;


import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romanpierson.vertx.web.accesslogger.AccessLoggerHandler;

import io.reactiverse.contextual.logging.ContextualData;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;
import sample.Utils;

public class SimpleJsonResponseVerticle extends AbstractVerticle {

	final Logger logger = LoggerFactory.getLogger(SimpleJsonResponseVerticle.class);
	
	final Random random = new Random();
	
	private final String configFile;
	
	public SimpleJsonResponseVerticle(String configFile) {
		this.configFile = configFile;
	}
	
	
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        
    	final Router router = Router.router(vertx);
    	
    	ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml")
				.setConfig(new JsonObject().put("path", configFile));

    	ConfigRetriever retriever = ConfigRetriever.create(vertx,  new ConfigRetrieverOptions().addStore(store));

    	retriever.getConfig(result -> {
    		
    		if(result.succeeded()) {
    			
    			JsonObject config = result.result();

    			// Determinate what port to use
    			final int serverPort = Utils.getConfiguredIntegerValue("server.port").orElse(8080);
    			
    			router.route("/metrics").handler(PrometheusScrapingHandler.create());
    			
    			registerRequiredHandlers(router, config);
    			
    			router.route().handler(this::addRequestIdToLoggingMdcIfPresent);
    			
    			router.get("/sleep/fixed/:sleepTimeMs").blockingHandler(this::handleFixedSleep, false);
    			router.post("/posttest").handler(this::handlePost);
    			router.get("/error").handler(this::handleError);
    			router.get("/empty").handler(this::handleEmpty);
    			router.get("/sleep/random/:sleepTimeMsFrom/:sleepTimeMsTo").blockingHandler(this::handleRandomSleep, false);
    			
    			vertx.createHttpServer().requestHandler(router).listen(serverPort);
    			
    			logger.info(String.format("Successfully started server at port %d", serverPort));
    			
    			startPromise.complete();
    		}
    		
    	});
    	
    }
    
    private void addRequestIdToLoggingMdcIfPresent(RoutingContext ctx) {
    	
    	final String requestId = ctx.request().getParam("requestId");

		if(requestId != null) {
			ContextualData.put("requestId", requestId);
		}
		
		ctx.next();
    	
    }
    
    private void registerRequiredHandlers(Router router, JsonObject config) {
    	
    	router
			.route()
			.handler(BodyHandler.create())
			.handler(AccessLoggerHandler.create(config))
			;
    }
    
    private void handlePost(RoutingContext ctx) {
	    final JsonObject resultJson = new JsonObject();
		
		resultJson.put("uri", ctx.request().uri());
	
		ctx.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson.encodePrettily());
    }
    
    private void handleEmpty(RoutingContext ctx) {
    	ctx.response().putHeader("Content-Type", "application/json; charset=utf-8").end();
    }
    
    private void handleError(RoutingContext ctx) {
    	
    	// We want to create a NPE
		if(true) {
			String s = null;
			s.trim();
		}
    }
    
    private void handleRandomSleep (RoutingContext ctx) {
    	
		 final String sleepTimeMsFromAsString = ctx.pathParam("sleepTimeMsFrom");
	    	final String sleepTimeMsToAsString = ctx.pathParam("sleepTimeMsTo");

			try {
				int randomSleep = getRandomBetween(Integer.parseInt(sleepTimeMsFromAsString), Integer.parseInt(sleepTimeMsToAsString));
				
				logger.info("Received call to sleep random {} ms", randomSleep);
				
				TimeUnit.MILLISECONDS.sleep(randomSleep);

			} catch (NumberFormatException | InterruptedException ex) {
				logger.error("Error in sleep endpoint", ex);
			}
			
			final JsonObject resultJson = new JsonObject();

			resultJson.put("uri", ctx.request().uri());

			ctx.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson.encodePrettily());
	
	 }
 
    private void handleFixedSleep (RoutingContext ctx) {
    	
    	final String sleepTimeMsAsString = ctx.pathParam("sleepTimeMs");

		try {
			
			long sleepInMs = Long.parseLong(sleepTimeMsAsString);
			
			logger.info("Received call to sleep fixed {} ms", sleepInMs);
			
			TimeUnit.MILLISECONDS.sleep(sleepInMs);
			
			
		} catch (NumberFormatException | InterruptedException ex) {
			logger.error("Error in sleep endpoint", ex);
		}
		
		final JsonObject resultJson = new JsonObject();

		resultJson.put("uri", ctx.request().uri());

		ctx.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson.encodePrettily());
    	
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
    	
    	stopPromise.complete();
    	
    }
    
    private int getRandomBetween(int minimum, int maximum) {
    	
    	return minimum + random.nextInt((maximum - minimum) + 1);
    	
    }
}
