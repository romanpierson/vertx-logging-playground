package sample;

import com.romanpierson.vertx.web.SimpleJsonResponseVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class App extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  vertx.deployVerticle(new SimpleJsonResponseVerticle("config.yaml"), result -> {
		  
		  if(result.succeeded()) {
			  startPromise.complete();
		  } else {
			  startPromise.fail(result.cause());
		  }
		  
	  });
	  
  }
  
}