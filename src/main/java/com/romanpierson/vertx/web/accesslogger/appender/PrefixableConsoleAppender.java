/*
 * Copyright (c) 2016-2023 Roman Pierson
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 
 * which accompanies this distribution.
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.romanpierson.vertx.web.accesslogger.appender;

import com.romanpierson.vertx.web.accesslogger.AccessLoggerConstants;

import static com.romanpierson.vertx.web.accesslogger.util.FormatUtility.getStringifiedParameterValues;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * 
 * Demoing how you can create custom appender - just does same ConsoleAppender does with adding some more logging
 * 
 * @author Roman Pierson
 *
 */
public class PrefixableConsoleAppender implements Appender {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private final String resolvedPattern;
	private final String prefix;
	
	public PrefixableConsoleAppender(final JsonObject config){
		
		if(config.getString(AccessLoggerConstants.CONFIG_KEY_RESOLVED_PATTERN, "").trim().length() == 0){
			throw new IllegalArgumentException("resolvedPattern must not be empty");
		}
		
		if(config.getString("prefix", "").trim().length() == 0){
			throw new IllegalArgumentException("prefix must not be empty");
		}
		
		this.resolvedPattern = config.getString(AccessLoggerConstants.CONFIG_KEY_RESOLVED_PATTERN);
		this.prefix = config.getString("prefix");
		
		logger.info("Created PrefixableConsoleAppender with resolvedLogPattern [" + this.resolvedPattern + "] and prefix [" + this.prefix + "]");
		
	}
	
	@Override
	public void push(List<Object> rawAccessElementValues, Map<String, Object> internalValues) {
		
		final String formattedString = String.format(this.resolvedPattern, getStringifiedParameterValues(rawAccessElementValues));
			
		System.out.println(this.prefix + " " + formattedString);
			
	}
	
}
