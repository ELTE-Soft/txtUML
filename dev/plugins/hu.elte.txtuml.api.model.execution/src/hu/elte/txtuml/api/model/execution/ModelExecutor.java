package hu.elte.txtuml.api.model.execution;

import java.util.List;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.runtime.BaseModelExecutor;

/**
 * Used to configure and start a model execution through the txtUML modeling
 * API. Starts the model execution on its own thread(s) so it runs in parallel
 * with the thread which created the executor.
 * <p>
 * For a default model executor, call the {@link #create} method.
 * <p>
 * Model executors does <b>not</b> need to be thread-safe.
 * <p>
 * As a general rule, all methods of this interface will throw
 * {@link NullPointerException} if a null reference is given to them as a
 * parameter, unless the opposite is explicitly stated in the documentation of
 * the methods.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface ModelExecutor extends BaseModelExecutor, Runnable {

	/**
	 * Creates a default model executor engine without a name.
	 * <p>
	 * <b>Note:</b> Instantiates the
	 * {@link hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor}
	 * class.
	 */
	static ModelExecutor create() {
		return new DefaultModelExecutor();
	}

	/**
	 * Creates a default model executor engine with the given name.
	 * <p>
	 * <b>Note:</b> Instantiates the
	 * {@link hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor}
	 * class.
	 */
	static ModelExecutor create(String name) {
		return new DefaultModelExecutor(name);
	}

	/**
	 * Represents the life cycle of a model executor.
	 */
	public enum Status {
		CREATED, ACTIVE, TERMINATED
	}

	/**
	 * @return the name of this model executor
	 */
	String getName();

	/**
	 * @return the current status of this model executor
	 */
	Status getStatus();

	// manage

	/**
	 * Starts a model execution with the previously specified initialization and
	 * sets the status of this executor to {@link Status#ACTIVE}.
	 * <p>
	 * <b>Note:</b> This method returns instantly, without waiting for the
	 * initialization or the termination of this executor. Therefore in most
	 * cases, {@link #launch} or {@link run} is more useful.
	 * 
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor start() throws LockedModelExecutorException;

	/**
	 * A shorthand operation for {@link #setInitialization(Runnable)}.
	 * {@link #start()} (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor start(Runnable initialization) throws LockedModelExecutorException;

	/**
	 * The given initialization will run as part of the model on a model
	 * executor thread as the first step of the model execution (optional
	 * operation).
	 * <p>
	 * The initialization may do any actions that are valid in the model, like
	 * creating model objects, linking, sending signals, etc.
	 * <p>
	 * As certain model executor implementations may initialize themselves based
	 * on some given parameters, this is an optional operation.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setInitialization(Runnable initialization) throws LockedModelExecutorException;

	/**
	 * Sets the model executor to be shut down after the currently running and
	 * all scheduled actions have been performed and every non-external event
	 * caused by them have been processed and all {@link TerminatePredicate}s
	 * become true. To shut down the executor instantly, call
	 * {@link #shutdownNow}.
	 * <p>
	 * This method <b>does not</b> await the termination of the executor, it
	 * returns instantly.
	 * 
	 * @return this
	 * @see #awaitTermination
	 */
	ModelExecutor shutdown();

	/**
	 * Shuts down the model executor without waiting for any currently running
	 * or scheduled actions to perform (it does not wait for
	 * {@link TerminationPredicate}s either). In most cases, {@link #shutdown}
	 * should be called instead.
	 * <p>
	 * This method <b>does not</b> await the termination of the executor, it
	 * returns instantly.
	 * 
	 * @return this
	 * @see #awaitTermination
	 */
	ModelExecutor shutdownNow();

	/**
	 * Awaits the model execution to finish the specified initialization and
	 * only returns after; if the current thread is interrupted, this method
	 * still keeps waiting. Use {@link #awaitInitializationNoCatch()} if this is
	 * not the desired behavior.
	 * 
	 * @return this
	 */
	ModelExecutor awaitInitialization();

	/**
	 * Awaits the model execution to finish the specified initialization and
	 * only returns after; throws an exception if interrupted.
	 * 
	 * @return this
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 * @see #awaitInitialization()
	 */
	ModelExecutor awaitInitializationNoCatch() throws InterruptedException;

	/**
	 * Awaits the model execution to terminate and only returns after; if the
	 * current thread is interrupted, this method still keeps waiting. Use
	 * {@link #awaitTerminationNoCatch()} if this is not the desired behavior.
	 */
	void awaitTermination();

	/**
	 * Awaits the model execution to terminate and only returns after; throws an
	 * exception if interrupted.
	 * 
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 * @see #awaitTermination()
	 */
	void awaitTerminationNoCatch() throws InterruptedException;

	/**
	 * A shorthand operation for {@link #start()}.
	 * {@link #awaitInitialization()}.
	 * 
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor launch() throws LockedModelExecutorException;

	/**
	 * A shorthand operation for {@link #setInitialization(Runnable)}.
	 * {@link #start()}. {@link #awaitInitialization()} (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor launch(Runnable initialization) throws LockedModelExecutorException;

	/**
	 * A shorthand operation for {@link #start()}. {@link #shutdown()}.
	 * {@link #awaitTermination()}.
	 * 
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	@Override
	void run() throws LockedModelExecutorException;

	/**
	 * A shorthand operation for {@link #setInitialization(Runnable)}.
	 * {@link #start()}. {@link #shutdown()}. {@link #awaitTermination()}
	 * (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	void run(Runnable initialization) throws LockedModelExecutorException;

	// inherited methods

	@Override
	ModelExecutor addTerminationListener(Runnable listener);

	@Override
	ModelExecutor removeTerminationListener(Runnable listener);

	@Override
	ModelExecutor addTerminationBlocker(Object blocker);

	@Override
	ModelExecutor removeTerminationBlocker(Object blocker);

	// report

	/**
	 * Adds a new {@link TraceListener} to be called when certain (valid) events
	 * happen during the model execution.
	 * 
	 * @param listener
	 *            the listener to add
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor addTraceListener(TraceListener listener) throws LockedModelExecutorException;

	/**
	 * Adds a new {@link ErrorListener} to be called when errors are raised
	 * during the model execution.
	 * 
	 * @param listener
	 *            the listener to add
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor addErrorListener(ErrorListener listener) throws LockedModelExecutorException;

	/**
	 * Adds a new {@link WarningListener} to be called when warnings are raised
	 * during the model execution.
	 * 
	 * @param listener
	 *            the listener to add
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor addWarningListener(WarningListener listener) throws LockedModelExecutorException;

	/**
	 * Removes the given trace listener.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor removeTraceListener(TraceListener listener) throws LockedModelExecutorException;

	/**
	 * Removes the given error listener.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor removeErrorListener(ErrorListener listener) throws LockedModelExecutorException;

	/**
	 * Removes the given warning listener.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor removeWarningListener(WarningListener listener) throws LockedModelExecutorException;

	// settings

	/**
	 * Sets whether optional dynamic checks should be performed during model
	 * execution. These checks include checking lower bounds of multiplicities,
	 * checking whether the guards of two transitions from the same vertex are
	 * overlapping, etc.
	 * <p>
	 * These checks are performed by default.
	 * 
	 * @param newValue
	 *            whether optional dynamic checks should be performed
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setDynamicChecks(boolean newValue) throws LockedModelExecutorException;

	/**
	 * The model execution time helps testing txtUML models in the following
	 * way: when any time-related event inside the model is set to take
	 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
	 * <i>mul</i> milliseconds during model execution, where <i>mul</i> is the
	 * current execution time multiplier. This way, txtUML models might be
	 * tested at the desired speed.
	 * <p>
	 * Execution time multiplier is 1 by default.
	 * 
	 * @param newMultiplier
	 *            the new execution time multiplier
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setExecutionTimeMultiplier(double newMultiplier) throws LockedModelExecutorException;

	/**
	 * Sets whether executor's trace log has to be shown. By default, it is
	 * switched off.
	 * 
	 * @param newValue
	 *            whether executor's trace log has to be shown
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setTraceLogging(boolean newValue) throws LockedModelExecutorException;

	/**
	 * See {@link #addTraceListener}.
	 * 
	 * @return an unmodifiable view of the registered trace listeners
	 */
	List<TraceListener> getTraceListeners();

	/**
	 * See {@link #addErrorListener}.
	 * 
	 * @return an unmodifiable view of the registered error listeners
	 */
	List<ErrorListener> getErrorListeners();

	/**
	 * See {@link #addWarningListener}.
	 * 
	 * @return an unmodifiable view of the registered warning listeners
	 */
	List<WarningListener> getWarningListeners();

	/**
	 * See {@link #setDynamicChecks}.
	 * 
	 * @return whether the optional dynamic checks are currently switched on
	 */
	boolean dynamicChecks();

	/**
	 * See {@link #setExecutionTimeMultiplier}.
	 * 
	 * @return the current execution time multiplier
	 */
	double getExecutionTimeMultiplier();

	/**
	 * See {@link #setExecutorLog}.
	 * 
	 * @return whether execution log is switched on
	 */
	boolean traceLogging();

}
