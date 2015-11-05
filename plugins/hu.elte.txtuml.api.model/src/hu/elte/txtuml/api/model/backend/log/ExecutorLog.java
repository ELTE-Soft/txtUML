package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelExecutor;

import java.io.PrintStream;

/**
 * This class manages the runtime log of the executor.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public final class ExecutorLog {

	private volatile PrintStream out = System.out;
	private volatile PrintStream err = System.err;
	private volatile boolean logEvents = false;

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
	 * Sets target stream of simple messages.
	 */
	public void setOut(PrintStream out) {
		this.out = out;
	}

	/**
	 * Sets target stream of error messages.
	 */
	public void setErr(PrintStream err) {
		this.err = err;
	}

	/**
	 * Sets whether or not valid events should be logged.
	 */
	public void setLogEvents(boolean logEvents) {
		this.logEvents = logEvents;
	}

	/**
	 * Prints a simple message.
	 */
	void out(String message) {
		if (logEvents) {
			synchronized (out) {
				out.println(message);
			}
		}
	}

	/**
	 * Prints an error message.
	 */
	void err(String message) {
		synchronized (err) {
			err.println(message);
		}
	}

	/**
	 * Prints a warning message.
	 */
	void warn(String message) {
		synchronized (err) {
			err.println(message);
		}
	}

}
