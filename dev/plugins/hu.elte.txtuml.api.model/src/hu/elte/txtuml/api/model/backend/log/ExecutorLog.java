package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.utils.Logger;

/**
 * This class manages the runtime log of the executor.
 */
public final class ExecutorLog {

	public ExecutorLog() {
		ModelExecutor.Report.addModelExecutionEventsListener(new ModelExecutionEventsListenerImpl(this));
		ModelExecutor.Report.addRuntimeWarningsListener(new RuntimeWarningsListenerImpl(this));
		ModelExecutor.Report.addRuntimeErrorsListener(new RuntimeErrorsListenerImpl(this));
	}

	/**
	 * Sets whether or not valid events should be logged.
	 */
	public void setLogEvents(boolean logEvents) {
		Logger.executor.setTracing(logEvents);
	}

	/**
	 * Prints a simple message.
	 */
	void out(String message) {
		Logger.executor.trace(message);
	}

	/**
	 * Prints an error message.
	 */
	void err(String message) {
		Logger.executor.error(message);
	}

	/**
	 * Prints a warning message.
	 */
	void warn(String message) {
		Logger.executor.warn(message);
	}

}
