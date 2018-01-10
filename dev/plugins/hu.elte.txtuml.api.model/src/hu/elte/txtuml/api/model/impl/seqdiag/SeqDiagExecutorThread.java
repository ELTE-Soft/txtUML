package hu.elte.txtuml.api.model.impl.seqdiag;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;

/**
 * All model executor threads that execute a sequence diagram must implement
 * this interface.
 */
public interface SeqDiagExecutorThread extends ExecutorThread, ImplRelated {

	/**
	 * Gets the current sequence diagram executor thread.
	 * 
	 * @return the current sequence diagram executor thread
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the caller thread is not a sequence diagram executor
	 *             thread
	 */
	static SeqDiagExecutorThread current() throws NotSeqDiagExecutorThreadError {
		return cast(Thread.currentThread());
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

	/**
	 * Casts the given object as a sequence diagram executor thread, if
	 * possible.
	 * 
	 * @return the given object as a sequence diagram executor thread
	 * @throws NotSeqDiagExecutorThreadError
	 *             if the given object is not a sequence diagram executor thread
	 */
	static SeqDiagExecutorThread cast(Object t) throws NotSeqDiagExecutorThreadError {
		if (t instanceof SeqDiagExecutorThread) {
			return (SeqDiagExecutorThread) t;
		}
		throw new NotSeqDiagExecutorThreadError();
	}

	SeqDiagRuntime getSeqDiagRuntime();

}
