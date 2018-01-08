package hu.elte.txtuml.api.model.execution;

import java.util.function.Consumer;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.external.BaseModelExecutor;

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
	 * Starts a model execution with the previously specified initialization,
	 * sets the status of this executor to {@link Status#ACTIVE} and awaits its
	 * initialization to complete.
	 * <p>
	 * <i>Note:</i> A shorthand operation for {@link #startNoWait()}&#x2e;
	 * {@link #awaitInitialization()}.
	 * 
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor start() throws LockedModelExecutorException;

	/**
	 * Sets the initialization of this model executor then starts it and awaits
	 * its initialization to complete (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * <p>
	 * <i>Note:</i> A shorthand operation for
	 * {@link #setInitialization(Runnable)}&#x2e;{@link #start()}.
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
	 * all scheduled actions have been performed, every non-external event
	 * caused by them have been processed and all <i>termination blockers</i> of
	 * this model executor have been removed. To shut down the executor
	 * instantly, call {@link #shutdownNow}.
	 * <p>
	 * This method <b>does not</b> await the termination of the executor, it
	 * returns instantly.
	 * 
	 * @return this
	 * @see #awaitTermination
	 * @see #addTerminationBlocker(Object)
	 * @see #removeTerminationBlocker(Object)
	 */
	ModelExecutor shutdown();

	/**
	 * Shuts down the model executor without waiting for any currently running
	 * or scheduled actions to perform (it does not wait for <i>termination
	 * blockers</i> of this model executor to be removed either). In most cases,
	 * {@link #shutdown} should be called instead.
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
	 * Starts a model execution with the previously specified initialization and
	 * sets the status of this executor to {@link Status#ACTIVE}.
	 * <p>
	 * This method returns instantly, without waiting for the initialization or
	 * the termination of this executor. Therefore in most cases, {@link #start}
	 * or {@link run} should be used.
	 * 
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor startNoWait() throws LockedModelExecutorException;

	/**
	 * Sets the initialization of this model executor then starts it and awaits
	 * its initialization to complete (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * <p>
	 * This method returns instantly, without waiting for the initialization or
	 * the termination of this executor. Therefore in most cases, {@link #start}
	 * or {@link run} should be used.
	 * <p>
	 * <i>Note:</i> A shorthand operation for
	 * {@link #setInitialization(Runnable)}&#x2e;{@link #startNoWait()}.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor startNoWait(Runnable initialization) throws LockedModelExecutorException;

	/**
	 * Awaits the model execution to terminate and only returns after; if the
	 * current thread is interrupted, this method still keeps waiting. Use
	 * {@link #awaitTerminationNoCatch()} if this is not the desired behavior.
	 */
	default void awaitTermination() {
		while (true) {
			try {
				awaitTerminationNoCatch();
				return;
			} catch (InterruptedException e) {
			}
		}
	}

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
	 * Starts this model executor, calls its {@link #shutdown} method and awaits
	 * its termination.
	 * <p>
	 * <i>Note:</i> A shorthand operation for
	 * {@link #startNoWait()}&#x2e;{@link #shutdown()}&#x2e;
	 * {@link #awaitTermination()}.
	 * 
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	@Override
	default void run() throws LockedModelExecutorException {
		startNoWait().shutdown().awaitTermination();
	}

	/**
	 * Sets the initialization of this model executor then starts it, calls its
	 * {@link #shutdown} method and awaits its termination (optional operation).
	 * <p>
	 * Supported iff {@link #setInitialization(Runnable)} is supported.
	 * <p>
	 * <i>Note:</i> A shorthand operation for
	 * {@link #setInitialization(Runnable)}&#x2e;{@link #run()}.
	 * 
	 * @param initialization
	 *            the initialization to run
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	default void run(Runnable initialization) throws LockedModelExecutorException {
		setInitialization(initialization).run();
	}

	// inherited methods with more specific return type

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
	 * Enables the modification of this model executor's settings. The settings
	 * object received in the {@code consumer} represents the current state of
	 * this model executor's settings and therefore only those fields have to be
	 * set which should be modified.
	 * <p>
	 * Note that the object received in the {@code consumer} action will be
	 * copied, that is, its modification after the execution of this method will
	 * have no effect on this model executor.
	 * 
	 * @param consumer
	 *            an action which modifies the settings of this model executor
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor set(Consumer<Execution.Settings> consumer) throws LockedModelExecutorException;

	/**
	 * Sets which level of model execution logs should be shown.
	 * <p>
	 * Log level is {@link LogLevel#WARNING} by default.
	 * 
	 * @param logLevel
	 *            the new log level
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setLogLevel(LogLevel logLevel) throws LockedModelExecutorException;

	/**
	 * Sets which level of dynamic checks should be performed during the model
	 * execution.
	 * <p>
	 * Check level is {@link CheckLevel#OPTIONAL} by default.
	 * 
	 * @param checkLevel
	 *            the new check level
	 * @return this
	 * @throws LockedModelExecutorException
	 *             if the model execution has already been started
	 */
	ModelExecutor setCheckLevel(CheckLevel checkLevel) throws LockedModelExecutorException;

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
	 * Returns the current settings of this model executor; the returned
	 * settings object is a copy: its modification has no effect on this
	 * executor.
	 */
	Execution.Settings getSettings();

	/**
	 * A shorthand operation for {@link #getSettings()}&#x2e;
	 * {@link ExecutionSettings#logLevel logLevel}.
	 * 
	 * @return the current log level
	 */
	default LogLevel getLogLevel() {
		return getSettings().logLevel;
	}

	/**
	 * A shorthand operation for {@link #getSettings()}&#x2e;
	 * {@link ExecutionSettings#checkLevel checkLevel}.
	 * 
	 * @return the current check level
	 */
	default CheckLevel getCheckLevel() {
		return getSettings().checkLevel;
	}

	/**
	 * A shorthand operation for {@link #getSettings()}&#x2e;
	 * {@link ExecutionSettings#timeMultiplier timeMultiplier}.
	 * 
	 * @return the current execution time multiplier
	 */
	default double getExecutionTimeMultiplier() {
		return getSettings().timeMultiplier;
	}

}
