package hu.elte.txtuml.api.model.execution.log;

import hu.elte.txtuml.utils.Logger;

/**
 * Base type for logger classes which write into the {@link Logger#executor}
 * log.
 */
public abstract class LoggerBase {

	private final String prefix;

	public LoggerBase(String nameOfExecutor) {
		if (nameOfExecutor == null || nameOfExecutor.equals("")) {
			prefix = "";
		} else {
			this.prefix = (nameOfExecutor + ": ").intern();
		}
	}

	/**
	 * Logs a trace message.
	 */
	protected final void trace(String message) {
		Logger.executor.trace(prefix + message);
	}

	/**
	 * Logs an error message.
	 */
	protected final void error(String message) {
		Logger.executor.error(prefix + message);
	}

	/**
	 * Logs a warning message.
	 */
	protected final void warn(String message) {
		Logger.executor.warn(prefix + message);
	}

}
