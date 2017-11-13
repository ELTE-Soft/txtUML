package hu.elte.txtuml.api.model.execution.log;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.execution.TraceListener;

/**
 * Writes into the {@link hu.elte.txtuml.utils.Logger#executor} log about every
 * trace event of a model executor.
 */
public class TraceLogger extends LoggerBase implements TraceListener {

	public TraceLogger(String nameOfExecutor) {
		super(nameOfExecutor);
	}

	@Override
	public void executionStarted() {
		trace("Model executor started.");
	}

	@Override
	public void processingSignal(ModelClass object, Signal signal, Boolean isAPI) {
		trace(object + " processes " + signal);
	}

	@Override
	public void usingTransition(ModelClass object, Transition transition) {
		trace(object + " uses " + transition);
	}

	@Override
	public void enteringVertex(ModelClass object, Vertex vertex) {
		trace(object + " enters " + vertex);
	}

	@Override
	public void leavingVertex(ModelClass object, Vertex vertex) {
		trace(object + " leaves " + vertex);
	}

	@Override
	public void executionTerminated() {
		trace("Model executor terminated.");
	}

}
