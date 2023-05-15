package com.ulwx.tool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class Log4j2Test {

	private static final Logger logger = LoggerFactory.getLogger(Log4j2Test.class);
	public static void main(String[] args) {
		
		MDC.put("clientNumber" , "12345");  
		logger.debug("xxxx");
	}
}
