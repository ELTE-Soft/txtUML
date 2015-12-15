package hu.elte.txtuml.utils;

public class Logger {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Logger.class);
	
	public static void logError(String message) {
		LOGGER.error(message);
	}
	
	public static void logError(String message, Throwable error) {
		LOGGER.error(message, error);
	}
	
	public static void logInfo(String message) {
		LOGGER.info(message);
	}

	public static void logWarning(String message) {
		LOGGER.warn(message);
	}
	
}
