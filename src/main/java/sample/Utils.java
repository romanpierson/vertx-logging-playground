package sample;

import java.util.Optional;

public class Utils {

	private Utils() {}
	
	public static Optional<Integer> getConfiguredIntegerValue(String key) {
		  
		  // First try to find the value on properties
		  
		  String valueAsString = System.getProperty(key);
		 
		  if(valueAsString == null) {
			  // See if we can get if from env
			  valueAsString = System.getenv(key);
		  }
		  
		  if(valueAsString == null) {
			  return Optional.empty();
		  }
		  
		  try {
			  return Optional.of(Integer.parseInt(valueAsString));
		  }catch(Exception ex) {
			  throw new RuntimeException("Error when trying to read int value for key [" + key + "] and raw value [" + valueAsString + "]", ex);
		  }
	  }
	
	
}
