package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.error.MissingRuntimeContextError;

/**
 * Through this interface some management features of the currently operating
 * model executor can be accessed in a thread-safe way.
 * <p>
 * These features are those which has to be provided by <i>any</i> txtUML model
 * executor and therefore <i>these and only these</i> can be used to implement
 * external libraries for txtUML models.
 */
public interface BaseModelExecutor {

	/**
	 * Gets the current model executor which is associated with the current
	 * thread.
	 * <p>
	 * <b>Note:</b> calls {@link Runtime#currentRuntime} and
	 * {@link Runtime#getExecutor}.
	 * 
	 * @return the model executor which is associated with the current thread
	 * @throws MissingRuntimeContextError
	 *             if the caller thread does not implement
	 *             {@link RuntimeContext}
	 */
	static BaseModelExecutor currentExecutor() throws MissingRuntimeContextError {
		return Runtime.currentRuntime().getExecutor();
	}

	/**
	 * Registers the specified {@link Runnable} to be run when the model
	 * execution is terminated. If the model execution is already terminated,
	 * the listener will run instantly, before this method returns.
	 * <p>
	 * The listener <b>will not</b> necessarily run on the caller's thread.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param listener
	 *            the action to be run when the executor is terminated
	 * @return this
	 */
	BaseModelExecutor addTerminationListener(Runnable listener);

	/**
	 * Removes the specified {@link Runnable} if it was previously added to the
	 * list of actions which are performed when the model execution is
	 * terminated.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param listener
	 *            the action to be removed
	 * @return this
	 */
	BaseModelExecutor removeTerminationListener(Runnable listener);

	/**
	 * TODO documentation
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param blocker
	 *            the blocker to be added
	 * @return this
	 */
	BaseModelExecutor addTerminationBlocker(Object blocker);

	/**
	 * TODO documentation
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param blocker
	 *            the blocker to be removed
	 * @return this
	 */
	BaseModelExecutor removeTerminationBlocker(Object blocker);

}
