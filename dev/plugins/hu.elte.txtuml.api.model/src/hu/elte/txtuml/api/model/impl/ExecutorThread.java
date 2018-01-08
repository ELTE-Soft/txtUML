package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.error.WrongModelExecutorThreadError;

/**
 * Base type for model executor threads. All model code has to run on a thread
 * that implements this interface.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface ExecutorThread extends ImplRelated {

	/**
	 * Gets the current model executor thread.
	 * 
	 * @return the current model executor thread
	 * @throws NotModelExecutorThreadError
	 *             if the caller thread is not a model executor thread
	 */
	static ExecutorThread current() throws NotModelExecutorThreadError {
		Thread t = Thread.currentThread();
		if (t instanceof ExecutorThread) {
			return (ExecutorThread) t;
		}
		throw new NotModelExecutorThreadError();
	}

	/**
	 * Ensures that the caller thread is a model executor thread.
	 * <p>
	 * Just an alias to {@link #current()} without a returned value.
	 * 
	 * @throws NotModelExecutorThreadError
	 *             if the caller thread is not a model executor thread
	 */
	static void requirePresence() throws NotModelExecutorThreadError {
		current();
	}

	/**
	 * Ensures that the specified model element is owned by this thread.
	 * 
	 * @throws WrongModelExecutorThreadError
	 *             if {@code element} is not owned by this thread
	 */
	default void requireOwned(RequiresRuntime<?> element) throws WrongModelExecutorThreadError {
		if (element != null) {
			requireOwned(getRuntimeOf(element));
		}
	}

	/**
	 * Ensures that the specified model elements are owned by this thread.
	 * 
	 * @throws WrongModelExecutorThreadError
	 *             if one (or more) of {@code elements} is not owned by this
	 *             thread
	 */
	default void requireOwned(RequiresRuntime<?>... elements) throws WrongModelExecutorThreadError {
		for (RequiresRuntime<?> e : elements) {
			requireOwned(e);
		}
	}

	/**
	 * Ensures that the specified runtime wrapper is owned by this thread.
	 * 
	 * @throws WrongModelExecutorThreadError
	 *             if {@code wrapper} is not owned by this thread
	 */
	default void requireOwned(RequiresExecutorThread wrapper) throws WrongModelExecutorThreadError {
		if (wrapper != null && wrapper.getThread() != this) {
			throw new WrongModelExecutorThreadError();
		}
	}

	/**
	 * Ensures that the specified runtime wrapper is owned by this thread.
	 * 
	 * @throws WrongModelExecutorThreadError
	 *             if one (or more) of {@code wrappers} is not owned by this
	 *             thread
	 */
	default void requireOwned(RequiresExecutorThread... wrappers) throws WrongModelExecutorThreadError {
		for (RequiresExecutorThread e : wrappers) {
			requireOwned(e);
		}
	}

	/**
	 * Returns the model runtime instance associated with the current model
	 * execution.
	 */
	ModelRuntime getModelRuntime();

}
