package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.backend.log.ExecutorLog;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;
import hu.elte.txtuml.api.model.report.RuntimeErrorsListener;
import hu.elte.txtuml.api.model.report.RuntimeWarningsListener;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class that manages the model execution.
 * 
 * <p>
 * <b>Represents:</b> no model element
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * The model executor starts automatically when this class is accessed from
 * code. To shut it down, call either {@link #shutdown} or {@link #shutdownNow}.
 * <p>
 * See {@link ModelExecutor.Settings} to change settings of the execution.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Implementation note:</b>
 * <p>
 * Accessed from multiple threads, so must be thread-safe.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public final class ModelExecutor implements ModelElement {

	/**
	 * The thread on which the model execution will run.
	 */
	private static final ModelExecutorThread thread = new ModelExecutorThread();

	/**
	 * A queue of actions to be performed when the executor is shut down.
	 */
	private static final Queue<Runnable> shutdownQueue = new ConcurrentLinkedQueue<>();

	private static final ExecutorLog executorLog = new ExecutorLog();

	/**
	 * Sole constructor of <code>ModelExecutor</code>, which is designed to be
	 * an uninstantiatable class.
	 */
	private ModelExecutor() {
	}

	// SETTINGS

	/**
	 * Provides all the global settings of txtUML model execution through its
	 * static methods.
	 * 
	 * <p>
	 * <b>Represents:</b> no model element
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Calling its static methods only affect the model execution, they are not
	 * exported to EMF-UML2.
	 * <p>
	 * Its static methods might be called from anywhere in the model as they are
	 * not harmful to the model execution in any way. However, it is strongly
	 * advised to perform any changes in the settings before the model execution
	 * is started to prevent confusing and unexpected results.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public static final class Settings implements ModelElement {

		/**
		 * The stream on which the user's non-error log is printed. Is
		 * {@link System#out} by default.
		 */
		private static volatile PrintStream userOutStream = System.out;

		/**
		 * The stream on which the user's error log is printed. Is
		 * {@link System#err} by default.
		 */
		private static volatile PrintStream userErrorStream = System.err;

		/**
		 * Indicates whether optional dynamic checks should be performed. Is
		 * <code>true</code> by default.
		 */
		private static volatile boolean dynamicChecks = true;

		/**
		 * A lock on {@link Settings#executionTimeMultiplier}.
		 */
		private static final Object LOCK_ON_EXECUTION_TIME_MULTIPLIER = new Object();

		/**
		 * The current execution time multiplier which affects how long
		 * time-related events should take.
		 */
		private static float executionTimeMultiplier = 1f;

		/**
		 * Indicates whether the execution time multiplier can still be changed
		 * or it has already been locked by calling
		 * {@link #lockExecutionTimeMultiplier()}.
		 */
		private static volatile boolean canChangeExecutionTimeMultiplier = true;

		/**
		 * Sole constructor of <code>Settings</code>, which is designed to be an
		 * uninstantiatable class.
		 */
		private Settings() {
		}

		/**
		 * Sets the stream on which the user's non-error log is printed. The
		 * user may write non-error log any time during model execution by
		 * calling the {@link Action#log(String) log} method of the action
		 * language.
		 * <p>
		 * By default, user's non-error log is printed on {@link System#out}.
		 * 
		 * @param userOutStream
		 *            the stream on which the user's non-error log will be
		 *            printed in the future
		 */
		public static void setUserOutStream(PrintStream userOutStream) {
			Settings.userOutStream = userOutStream;
		}

		/**
		 * Sets the stream on which the user's error log is printed. The user
		 * may write error log any time during model execution by calling the
		 * {@link Action#logError(String) logError} method of the action
		 * language.
		 * <p>
		 * By default, user's error log is printed on {@link System#err}.
		 * 
		 * @param userErrorStream
		 *            the stream on which the user's error log will be printed
		 *            in the future
		 */
		public static void setUserErrorStream(PrintStream userErrorStream) {
			Settings.userErrorStream = userErrorStream;
		}

		/**
		 * Sets the stream on which the executor's non-error log is printed.
		 * Executor's non-error log consists of short messages reporting about
		 * certain changes in the runtime model, like a model object processing
		 * a signal or using a transition. Executor's non-error log can be
		 * turned on or off by calling the
		 * {@link Settings#setExecutorLog(boolean) setExecutorLog} method.
		 * <p>
		 * By default, executor's non-error log is printed on {@link System#out}.
		 * 
		 * @param executorOutStream
		 *            the stream on which the executor's non-error log will be
		 *            printed in the future
		 */
		public static void setExecutorOutStream(PrintStream executorOutStream) {
			executorLog.setOut(executorOutStream);
		}

		/**
		 * Sets the stream on which the executor's error log is printed.
		 * Executor's error log consists of runtime errors and warnings.
		 * <p>
		 * By default, executor's error log is printed on {@link System#err}.
		 * 
		 * @param executorErrorStream
		 *            the stream on which the executor's error log will be
		 *            printed in the future
		 */
		public static void setExecutorErrorStream(
				PrintStream executorErrorStream) {
			executorLog.setErr(executorErrorStream);
		}

		/**
		 * Sets whether executor's non-error log has to be shown. By default, it
		 * is switched off.
		 * <p>
		 * Executor's non-error log consists of short messages reporting about
		 * certain changes in the runtime model, like a model object processing
		 * a signal or using a transition.
		 * 
		 * @param newValue
		 *            whether executor's non-error log has to shown
		 */
		public static void setExecutorLog(boolean newValue) {
			executorLog.setLogEvents(newValue);
		}

		/**
		 * Sets whether optional dynamic checks should be performed during model
		 * execution. These checks include checking lower bounds of
		 * multiplicities, checking whether the guards of two transitions from
		 * the same vertex are overlapping, etc.
		 * <p>
		 * These checks are performed by default.
		 * 
		 * @param newValue
		 *            whether optional dynamic checks should be performed
		 */
		public static void setDynamicChecks(boolean newValue) {
			Settings.dynamicChecks = newValue;
		}

		/**
		 * The model execution time helps testing txtUML models in the following
		 * way: when any time-related event inside the model is set to take
		 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
		 * <i>mul</i> millseconds during model execution, where <i>mul</i> is
		 * the current execution time multiplier. This way, txtUML models might
		 * be tested at the desired speed.
		 * <p>
		 * Execution time multiplier is 1 by default.
		 * 
		 * @param newMultiplier
		 *            the new execution time multiplier
		 */
		public static void setExecutionTimeMultiplier(float newMultiplier) {
			synchronized (Settings.LOCK_ON_EXECUTION_TIME_MULTIPLIER) {
				if (Settings.canChangeExecutionTimeMultiplier) {
					Settings.executionTimeMultiplier = newMultiplier;
				} else {
					Report.error.forEach(x -> x.changingLockedExecutionTimeMultiplier());
				}
			}
		}

		/**
		 * The model execution time helps testing txtUML models in the following
		 * way: when any time-related event inside the model is set to take
		 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
		 * <i>mul</i> millseconds during model execution, where <i>mul</i> is
		 * the current execution time multiplier. This way, txtUML models might
		 * be tested at the desired speed.
		 * 
		 * @return the current execution time multiplier
		 * @see #setExecutionTimeMultiplier(float)
		 */
		public static float getExecutionTimeMultiplier() {
			return Settings.executionTimeMultiplier;
		}

		/**
		 * Provides a conversion from <i>'real time'</i> to model execution time
		 * by multiplying its parameter with the execution time multiplier.
		 * <p>
		 * The model execution time helps testing txtUML models in the following
		 * way: when any time-related event inside the model is set to take
		 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
		 * <i>mul</i> millseconds during model execution, where <i>mul</i> is
		 * the current execution time multiplier. This way, txtUML models might
		 * be tested at the desired speed.
		 * <p>
		 * Execution time multiplier is 1 by default.
		 * 
		 * @param time
		 *            the amount of time to be given in model execution time
		 * @return the specified amount of time in model execution time
		 * @see #setExecutionTimeMultiplier(float)
		 */
		public static long inExecutionTime(long time) {
			return (long) (time * executionTimeMultiplier);
		}

		/**
		 * Locks the execution time multiplier so that it may not be changed in
		 * the future. It should be called when a time-related event happens in
		 * the model to ensure that such events are not influenced by the change
		 * of this multiplier in a bad or unspecified way. If called multiple
		 * times, this method does nothing after the first call.
		 * <p>
		 * For example, the
		 * {@link hu.elte.txtuml.stdlib.Timer#start(ModelClass, Signal, ModelInt)
		 * Timer.start} method calls this method as it operates with timed
		 * events.
		 */
		public static void lockExecutionTimeMultiplier() {
			Settings.canChangeExecutionTimeMultiplier = false;
		}

		/**
		 * @return whether optional dynamic checks should be performed
		 */
		static boolean dynamicChecks() {
			return Settings.dynamicChecks;
		}

	}

	// REPORT

	public static final class Report {

		static final Queue<ModelExecutionEventsListener> event = new ConcurrentLinkedQueue<>();
		
		static final Queue<RuntimeErrorsListener> error = new ConcurrentLinkedQueue<>();

		static final Queue<RuntimeWarningsListener> warning = new ConcurrentLinkedQueue<>();
		
		public static void addModelExecutionEventsListener(ModelExecutionEventsListener listener) {
			event.add(listener);
		}
		
		public static void addRuntimeErrorsListener(RuntimeErrorsListener listener) {
			error.add(listener);
		}
		
		public static void addRuntimeWarningsListener(RuntimeWarningsListener listener) {
			warning.add(listener);
		}
		
		public static void removeModelExecutionEventsListener(ModelExecutionEventsListener listener) {
			event.remove(listener);
		}
		
		public static void removeRuntimeErrorsListener(RuntimeErrorsListener listener) {
			error.remove(listener);
		}
		
		public static void removeRuntimeWarningsListener(RuntimeWarningsListener listener) {
			warning.remove(listener);
		}
	}

	// EXECUTION

	/**
	 * Sets the model executor to be shut down after the currently running and
	 * all scheduled actions have been performed and every non-external event
	 * caused by them have been processed. To shut down the executor instantly,
	 * call {@link #shutdownNow}.
	 */
	public static void shutdown() {
		thread.shutdown();
	}

	/**
	 * Shuts down the model executor without waiting for any currently running
	 * or scheduled actions to perform. In most cases, {@link #shutdown} should
	 * be called instead.
	 */
	public static void shutdownNow() {
		Report.event.forEach(x -> x.executionTerminated());

		while (true) {
			Runnable shutdownAction = shutdownQueue.poll();
			if (shutdownAction == null) {
				break;
			}
			shutdownAction.run();
		}

		thread.interrupt();
	}

	/**
	 * Registers the specified {@link Runnable} to be run when the model
	 * executor is shut down.
	 * 
	 * @param shutdownAction
	 *            the action to be run when the executor is shut down
	 */
	public static void addToShutdownQueue(Runnable shutdownAction) {
		shutdownQueue.add(shutdownAction);
	}

	/**
	 * Sends a signal to the specified target object asynchronously.
	 * 
	 * @param target
	 *            the target object of the send operation
	 * @param signal
	 *            the signal to send
	 */
	static void send(ModelClass target, Signal signal) {
		thread.send(target, signal);
	}

	/**
	 * Registers a check of the lower bound of the specified association end's
	 * multiplicity to be performed in the next <i>execution step</i>.
	 * <p>
	 * The lower bound of multiplicities might be offended temporarily but has
	 * to be restored before returning from the current <i>execution step</i>.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 * 
	 * @param obj
	 *            the object on the opposite end of the association
	 * @param assocEnd
	 *            the association end which's multiplicity is to be checked
	 */
	static void checkLowerBoundInNextExecutionStep(ModelClass obj,
			Class<? extends AssociationEnd<?>> assocEnd) {

		thread.checkLowerBoundOfMultiplcitiy(obj, assocEnd);
	}

	// LOGGING METHODS

	/**
	 * Prints the specified message in the user's non-error log.
	 * 
	 * @param message
	 *            the message to print in the log
	 * @see Settings#setUserOutStream(PrintStream)
	 */
	static void log(String message) {
		logOnStream(Settings.userOutStream, message);
	}

	/**
	 * Prints the specified message in the user's error log.
	 * 
	 * @param message
	 *            the message to print in the log
	 * @see Settings#setUserErrorStream(PrintStream)
	 */
	static void logError(String message) {
		logOnStream(Settings.userErrorStream, message);
	}

	/**
	 * Prints on the given stream after synchronizing on it to ensure that
	 * different threads do not mix the output.
	 * 
	 * @param printStream
	 *            the stream to print on
	 * @param message
	 *            the message to print on the given stream
	 */
	private static void logOnStream(PrintStream printStream, String message) {
		synchronized (printStream) {
			printStream.println(message);
		}
	}
}
