package hu.elte.txtuml.utils;

public enum Logger {
	sys("SYS"), user("USER"), executor("EXEC");

	private final org.apache.log4j.Logger logger;

	private Logger(String name) {
		logger = org.apache.log4j.Logger.getLogger(name);
	}

	public final void fatal(String msg) {
		logger.fatal(msg);
	}

	public final void fatal(String msg, Throwable t) {
		logger.fatal(msg, t);
	}

	public final void error(String msg) {
		logger.error(msg);
	}

	public final void error(String msg, Throwable t) {
		logger.error(msg, t);
	}

	public final void warn(String msg) {
		logger.warn(msg);
	}

	public final void info(String msg) {
		logger.info(msg);
	}

	public final void debug(String msg) {
		logger.debug(msg);
	}

	public final void trace(String msg) {
		logger.trace(msg);
	}

}
