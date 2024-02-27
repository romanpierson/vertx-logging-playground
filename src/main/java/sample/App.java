package sample;


import com.romanpierson.vertx.elasticsearch.indexer.verticle.ElasticSearchIndexerVerticle;
import com.romanpierson.vertx.web.SimpleJsonResponseVerticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

public class App {

  private final static Logger LOG = LoggerFactory.getLogger(App.class);
	
  public static void main(String[] args) {
		
	  MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
		      .setEnabled(true)
		      .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true));
		    VertxOptions vertxOptions = new VertxOptions()
		      .setMetricsOptions(metricsOptions);
		    
	  final Vertx vertx = Vertx.vertx(vertxOptions);
	  
	  // First deploy the indexer verticle
	  // First get its configuration
	  
	  ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml")
				.setConfig(new JsonObject().put("path", "indexer-config.yaml"));

	  ConfigRetriever retriever = ConfigRetriever.create(vertx,  new ConfigRetrieverOptions().addStore(store));

	  retriever
	  	.getConfig()
	  	.onComplete(jsonConfig -> {
	  		
	  		// Now deploy the ElasticSearchIndexerVerticle
	  		vertx
	  			.deployVerticle(ElasticSearchIndexerVerticle.class.getName(), new DeploymentOptions().setConfig(jsonConfig))
			  	.onComplete(deploymentId -> {
				
			  		LOG.info("Successfully deployed ElasticSearchIndexerVerticle");
			  		
			  		// Now deploy the SimpleJsonResponseVerticle
			  		vertx
			  			.deployVerticle(new SimpleJsonResponseVerticle("config.yaml"))
			  			.onComplete(deploymentId2 -> {
			  				LOG.info("Successfully deployed SimpleJsonResponseVerticle");
			  			}, throwable -> {
			  				LOG.error("Error when trying to deploy SimpleJsonResponseVerticle", throwable);
			  			});
			  		
				}, throwable -> {
					LOG.error("Error when trying to deploy ElasticSearchIndexerVerticle", throwable);
				});
	  		
	  		
	  	}, throwable -> {
	  		LOG.error("Error when trying to read config for ElasticSearchIndexerVerticle", throwable);
	  	});
	  
	}
  
}