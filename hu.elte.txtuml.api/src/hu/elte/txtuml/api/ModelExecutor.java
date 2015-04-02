package hu.elte.txtuml.api;

import java.io.PrintStream;

public final class ModelExecutor implements ModelElement {
	/*
	 * Accessed from multiple threads, so must be thread-safe.
	 */

	private ModelExecutor() {
	}

	static ModelExecutorThread getExecutorThreadStatic() {
		return ModelExecutorThread.getSingletonInstance();
	}

	// SETTINGS

	public static final class Settings implements ModelElement {
		/*
		 * In the setters of the four streams, no synchronization is needed
		 * because the assignment is atomic and if a printing operation is
		 * active on another thread, that operation will completely finish on
		 * the old stream either way.
		 */
		
		private static PrintStream userOutStream = System.out;
		private static PrintStream userErrorStream = System.err;
		private static PrintStream executorOutStream = System.out;
		private static PrintStream executorErrorStream = System.err;
		private static boolean executorLog = false;
		private static boolean dynamicChecks = true;

		private static final Object LOCK_ON_EXECUTION_TIME_MULTIPLIER = new Object();
		
		private static long executionTimeMultiplier = 1;
		private static boolean canChangeExecutionTimeMultiplier = true;

		private Settings() {
		}

		public static void setUserOutStream(PrintStream userOutStream) {
			Settings.userOutStream = userOutStream;
		}

		public static void setUserErrorStream(PrintStream userErrorStream) {
			Settings.userErrorStream = userErrorStream;
		}

		public static void setExecutorOutStream(PrintStream executorOutStream) {
			Settings.executorOutStream = executorOutStream;
		}

		public static void setExecutorErrorStream(
				PrintStream executorErrorStream) {
			Settings.executorErrorStream = executorErrorStream;
		}

		public static void setExecutorLog(boolean newValue) {
			Settings.executorLog = newValue;
		}

		public static void setExecutionTimeMultiplier(long newMultiplier) {
			synchronized (Settings.LOCK_ON_EXECUTION_TIME_MULTIPLIER) {
				if (Settings.canChangeExecutionTimeMultiplier) {
					Settings.executionTimeMultiplier = newMultiplier;
				} else {
					// TODO show error
				}
			}
		}

		public static long getExecutionTimeMultiplier() {
			/*
			 * Reading a long value is not atomic, so synchronization is needed.
			 */
			synchronized (Settings.LOCK_ON_EXECUTION_TIME_MULTIPLIER) {
				return Settings.executionTimeMultiplier;
			}
		}

		public static void lockExecutionTimeMultiplier() {
			Settings.canChangeExecutionTimeMultiplier = false;
		}

		static boolean executorLog() {
			return Settings.executorLog;
		}
		
		static boolean dynamicChecks() {
			return Settings.dynamicChecks;
		}
	}

	// EXECUTION

	static void send(ModelClass target, Signal signal) {
		getExecutorThreadStatic().send(target, signal);
	}

	// LOGGING METHODS

	private static void logOnStream(PrintStream printStream, String message) {
		synchronized (printStream) {
			printStream.println(message);
		}
	}

	static void log(String message) {
		logOnStream(Settings.userOutStream, message);
	}

	static void logError(String message) {
		logOnStream(Settings.userErrorStream, message);
	}

	static void executorLog(String message) {
		logOnStream(Settings.executorOutStream, message);
	}

	static void executorErrorLog(String message) {
		logOnStream(Settings.executorErrorStream, message);
	}
}
