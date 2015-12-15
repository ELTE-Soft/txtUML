package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.utils.Logger;

/**
 * This class manages the runtime log of the executor.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public final class ExecutorLog {

	public ExecutorLog() {
		ModelExecutor.Report
				.addModelExecutionEventsListener(new ModelExecutionEventsListenerImpl(
						this));
		ModelExecutor.Report
				.addRuntimeWarningsListener(new RuntimeWarningsListenerImpl(
						this));
		ModelExecutor.Report
				.addRuntimeErrorsListener(new RuntimeErrorsListenerImpl(this));
	}
	
	/**
	 * Prints a simple message.
	 */
	void out(String message) {
		Logger.logInfo(message);
	}

	/**
	 * Prints an error message.
	 */
	void err(String message) {
		Logger.logError(message);
	}

	/**
	 * Prints a warning message.
	 */
	void warn(String message) {
		Logger.logWarning(message);
	}

}
