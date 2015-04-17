package hu.elte.txtuml.api;

import java.io.PrintStream;

public final class ModelExecutor implements ModelElement {
	/*
	 * Accessed from multiple threads, so must be thread-safe.
	 */

	private static final ModelExecutorThread thread = new ModelExecutorThread();

	/**
	 * Sole constructor of <code>ModelExecutor</code>, which is designed to be
	 * an uninstantiatable class.
	 */
	private ModelExecutor() {
	}

	// SETTINGS

	public static final class Settings implements ModelElement {

		/**
		 * The stream on which the user's non-error log is printed.
		 */
		private static volatile PrintStream userOutStream = System.out;

		/**
		 * The stream on which the user's error log is printed.
		 */
		private static volatile PrintStream userErrorStream = System.err;

		/**
		 * The stream on which the executor's non-error log is printed.
		 */
		private static volatile PrintStream executorOutStream = System.out;

		/**
		 * The stream on which the executor's error log is printed.
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
		private static long executionTimeMultiplier = 1;

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

		public static void setDynamicChecks(boolean newValue) {
			Settings.dynamicChecks = newValue;
		}

		/*
		 * The execution
		 * 
		 * @return the new execution time multiplier
		 */
		public static void setExecutionTimeMultiplier(long newMultiplier) {
			synchronized (Settings.LOCK_ON_EXECUTION_TIME_MULTIPLIER) {
				if (Settings.canChangeExecutionTimeMultiplier) {
					Settings.executionTimeMultiplier = newMultiplier;
				} else {
					// TODO show error
				}
			}
		}

		/*
		 * @return the current execution time multiplier
		 */
		public static long getExecutionTimeMultiplier() {
			// Reading a long value is not atomic, so synchronization is needed.
			synchronized (Settings.LOCK_ON_EXECUTION_TIME_MULTIPLIER) {
				return Settings.executionTimeMultiplier;
			}
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
