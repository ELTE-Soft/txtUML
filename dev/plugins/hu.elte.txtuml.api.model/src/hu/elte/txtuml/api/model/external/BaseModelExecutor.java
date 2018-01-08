package hu.elte.txtuml.api.model.external;

import java.util.function.Function;

import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.impl.ModelRuntime;

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
	 * <b>Note:</b> calls {@link ModelRuntime#current()}.
	 * {@link ModelRuntime#getExecutor getExecutor()}.
	 * 
	 * @return the model executor which is associated with the current thread
	 * @throws NotModelExecutorThreadError
	 *             if the caller thread is not a model executor thread
	 */
	static BaseModelExecutor current() throws NotModelExecutorThreadError {
		return ModelRuntime.current().getExecutor();
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
	 * Removes the specified termination blocker from this model executor's set
	 * of blockers. A model executor may only terminate gracefully if this set
	 * is empty, that is, if all previously added blockers are removed. A forced
	 * termination may omit this restriction.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param blocker
	 *            the blocker to be removed
	 * @return this
	 */
	BaseModelExecutor removeTerminationBlocker(Object blocker);

	/**
	 * Stores a specific feature for this model executor identified by the given
	 * key. The model executor does not use this feature for any purpose, the
	 * only use of this method is to associate services with this executor which
	 * can be later retrieved with the {@link #getFeature} method.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * <p>
	 * Note that although this method and the {@link #getFeature} method are
	 * both thread-safe, a code that calls {@link &getFeature} and then
	 * {@link #setFeature} (in order to initialize the feature if the
	 * {@link #getFeature} returned {@code null}) is <i><b>not</b></i>
	 * thread-safe. Use {@link #getOrCreateFeature} in such cases.
	 * 
	 * @param key
	 *            the key associated with the given feature in this model
	 *            executor
	 * @param feature
	 *            the feature to be stored
	 */
	void setFeature(Object key, Object feature);

	/**
	 * Returns a specific feature for this model executor identified by the
	 * given key or {@code null} if no such feature has been found.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param key
	 *            the key associated with the required feature in this model
	 *            executor
	 * @returns the stored feature or {@code null}
	 * 
	 * @see #setFeature
	 */
	Object getFeature(Object key);

	/**
	 * Returns a specific feature for this model executor identified by the
	 * given key or creates a new one with the provided supplier if no such
	 * feature has been found.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param key
	 *            the key associated with the required feature in this model
	 *            executor
	 * @param creator
	 *            a function that receives the key as a parameter and must
	 *            return the new value (not {@code null})
	 * @returns the stored feature or the newly created one
	 * 
	 * @see #setFeature
	 */
	Object getOrCreateFeature(Object key, Function<Object, Object> creator);

}
