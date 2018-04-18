package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;

/**
 * All model executor threads that execute a sequence diagram must implement
 * this interface.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
@SequenceDiagramRelated
public interface SeqDiagThread extends ImplRelated {

	/**
	 * Gets the current sequence diagram executor thread.
	 * 
	 * @return the current sequence diagram executor thread
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the caller thread is not a sequence diagram executor
	 *             thread
	 */
	static SeqDiagThread current() throws NotSeqDiagExecutorThreadError {
		Thread t = Thread.currentThread();
		if (t instanceof SeqDiagThread) {
			return (SeqDiagThread) t;
		}
		throw new NotSeqDiagExecutorThreadError();
	}

	/**
	 * Ensures that the caller thread is a sequence diagram executor thread.
	 * <p>
	 * Just an alias to {@link #current()} without a returned value.
	 * 
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the caller thread is not a sequence diagram executor
	 *             thread
	 */
	static void requirePresence() throws NotModelExecutorThreadError {
		current();
	}

	InteractionRuntime getCurrentInteraction();

}
