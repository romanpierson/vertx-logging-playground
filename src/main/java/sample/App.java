package sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romanpierson.vertx.elasticsearch.indexer.verticle.ElasticSearchIndexerVerticle;
import com.romanpierson.vertx.web.SimpleJsonResponseVerticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class App {

  private final static Logger logger = LoggerFactory.getLogger(App.class);
	
  public static void main(String[] args) {
		
	  Vertx vertx = Vertx.vertx();
	  
	  		// Then deploy the ElasticSearchIndexerVerticle
	  		ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml")
					.setConfig(new JsonObject().put("path", "indexer-config.yaml"));

	    	ConfigRetriever retriever = ConfigRetriever.create(vertx,  new ConfigRetrieverOptions().addStore(store));

	    	retriever
			.getConfig()
			.onComplete(indexerConfig -> {
				
				if(indexerConfig.succeeded()) {
					
					vertx.deployVerticle(ElasticSearchIndexerVerticle.class.getName(),
							new DeploymentOptions().setConfig(indexerConfig.result())).onComplete(deploymentId -> {
								
								// Now deploy our main verticle
								vertx
							  	.deployVerticle(new SimpleJsonResponseVerticle("config.yaml"))
							  	.onComplete(result2 -> {
									
							  		 if(result2.succeeded()) {
							  			 logger.info("Started successfully SimpleJsonResponseVerticle");
									  } else {
										  logger.error("Failed to start SimpleJsonResponseVerticle", result2.cause());
									  }
								  
							  	});
								
							}, throwable -> {
								throw new RuntimeException("Error when deploying ElasticSearchIndexerVerticle", throwable);
							});
				} else {
					indexerConfig.cause().printStackTrace();
				}
				
			});
	  		
	  
	}
  
}