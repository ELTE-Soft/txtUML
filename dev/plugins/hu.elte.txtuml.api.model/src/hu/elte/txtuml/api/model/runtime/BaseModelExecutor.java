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
 * <p>
 * As a general rule, all methods of this interface will throw
 * {@link NullPointerException} if a null reference is given to them as a
 * parameter, unless the opposite is explicitly stated in the documentation of
 * the methods.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface BaseModelExecutor {

	/**
	 * Gets the current model executor which is associated with the current
	 * thread.
	 * <p>
	 * <b>Note:</b> calls {@link Runtime#currentRuntime()}.
	 * {@link Runtime#getExecutor getExecutor()}.
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
	 * Adds a termination blocker to this model executor's set of blockers. A
	 * model executor may only terminate gracefully if this set is empty, that
	 * is, if all previously added blockers are removed. A forced termination
	 * may omit this restriction.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param blocker
	 *            the blocker to be added
	 * @return this
	 */
	BaseModelExecutor addTerminationBlocker(Object blocker);

	/**
	 * Removes a termination blocker from this model executor's set of blockers.
	 * A model executor may only terminate gracefully if this set is empty, that
	 * is, if all previously added blockers are removed. A forced termination
	 * may omit this restriction.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param blocker
	 *            the blocker to be removed
	 * @return this
	 */
	BaseModelExecutor removeTerminationBlocker(Object blocker);

}
