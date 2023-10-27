package sample;

import com.romanpierson.vertx.web.SimpleJsonResponseVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class App {

  private final static Logger logger = LoggerFactory.getLogger(App.class);
	
  public static void main(String[] args) {
		
	  Vertx.vertx()
	  	.deployVerticle(new SimpleJsonResponseVerticle("config.yaml"))
	  	.onComplete(result -> {
			
	  		 if(result.succeeded()) {
	  			 logger.info("Started successfully SimpleJsonResponseVerticle");
			  } else {
				  logger.error("Failed to start SimpleJsonResponseVerticle", result.cause());
			  }
		  
	  	});
	  
	}
  
}