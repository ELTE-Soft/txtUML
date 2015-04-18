package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.messages.ErrorMessages;

import java.io.PrintStream;

/**
 * The class that manages the model execution.
 * 
 * <p>
 * <b>Represents:</b> no model element
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
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
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
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
	 * Sole constructor of <code>ModelExecutor</code>, which is designed to be
	 * an uninstantiatable class.
	 */
	private ModelExecutor() {
	}

	// SETTINGS

	/**
	 * This class provides all the global settings of txtUML model execution
	 * through its static methods.
	 * 
	 * <p>
	 * <b>Represents:</b> no model element
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Calling its static methods only affect the model execution, they are not
	 * exported to UML2.
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
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
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
		 * The stream on which the executor's non-error log is printed. Is
		 * {@link System#out} by default.
		 */
		private static volatile PrintStream executorOutStream = System.out;

		/**
		 * The stream on which the executor's error log is printed. Is
		 * {@link System#err} by default.
		 */
		private static volatile PrintStream executorErrorStream = System.err;

		/**
		 * Indicates whether executor's non-error log is active (whether it
		 * should be printed). Is <code>false</code> by default.
		 */
		private static volatile boolean executorLog = false;

		/**
		 * Indicates whether non-necessary dynamic checks should be performed.
		 * Is <code>true</code> by default.
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
			Settings.executorOutStream = executorOutStream;
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
			Settings.executorErrorStream = executorErrorStream;
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
			Settings.executorLog = newValue;
		}

		/**
		 * Sets whether non-necessary dynamic checks should be performed during
		 * model execution. These checks include checking lower bounds of
		 * multiplicities, checking whether the guards of two transitions from
		 * the same vertex are overlapping, etc.
		 * <p>
		 * These checks are performed by default.
		 * 
		 * @param newValue
		 *            whether non-necessary dynamic checks should be performed
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
					executorErrorLog(ErrorMessages
							.getChangingLockedExecutionTimeMultiplierMessage());
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
		 * @return whether executor's non-error log is active (whether it should
		 *         be printed)
		 */
		static boolean executorLog() {
			return Settings.executorLog;
		}

		/**
		 * @return whether non-necessary dynamic checks should be performed
		 */
		static boolean dynamicChecks() {
			return Settings.dynamicChecks;
		}

	}

	// EXECUTION

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
	 * multiplicity to be performed in the next execution step.
	 * <p>
	 * The lower bound of multiplicities might be offended temporarily but has
	 * to be restored before returning from the current execution step.
	 * <p>
	 * An execution step starts when an asynchronous event (like a signal event)
	 * is chosen by the executor to be processed and ends when that event and
	 * all the synchronous events caused by it (like a state machine changing
	 * state, entry and exit actions, transition effects, operation calls,
	 * etc.), have been processed.
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
	 * Prints the specified message in the executor's non-error log.
	 * 
	 * @param message
	 *            the message to print in the log
	 * @see Settings#setExecutorOutStream(PrintStream)
	 */
	static void executorLog(String message) {
		logOnStream(Settings.executorOutStream, message);
	}

	/**
	 * Prints the specified message in the executor's error log.
	 * 
	 * @param message
	 *            the message to print in the log
	 * @see Settings#setExecutorErrorStream(PrintStream)
	 */
	static void executorErrorLog(String message) {
		logOnStream(Settings.executorErrorStream, message);
	}

	/**
	 * Prints on the given stream after synchronizing on it to ensure that
	 * different threads do not mix the output.
	 * 
	 * @param printStream
	 *            the stream to print on
	 * @param message
	 *            the to print on the given stream
	 */
	private static void logOnStream(PrintStream printStream, String message) {
		synchronized (printStream) {
			printStream.println(message);
		}
	}
}
