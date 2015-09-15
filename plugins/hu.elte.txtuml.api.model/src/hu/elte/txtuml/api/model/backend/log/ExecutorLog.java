package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelExecutor;

import java.io.PrintStream;

public final class ExecutorLog {

	private volatile PrintStream out = System.out;
	private volatile PrintStream err = System.err;
	private volatile boolean logEvents = false;

	public ExecutorLog() {
		ModelExecutor.Report.addModelExecutionEventsListener(new ModelExecutionEventsListenerImpl(this));
		ModelExecutor.Report.addRuntimeWarningsListener(new RuntimeWarningsListenerImpl(this));
		ModelExecutor.Report.addRuntimeErrorsListener(new RuntimeErrorsListenerImpl(this));
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public void setErr(PrintStream err) {
		this.err = err;
	}

	public void setLogEvents(boolean logEvents) {
		this.logEvents = logEvents;
	}

	void out(String message) {
		if (logEvents) {
			synchronized (out) {
				out.println(message);
			}
		}
	}

	void err(String message) {
		synchronized (err) {
			err.println(message);
		}
	}

	void warn(String message) {
		synchronized (err) {
			err.println(message);
		}
	}

}
