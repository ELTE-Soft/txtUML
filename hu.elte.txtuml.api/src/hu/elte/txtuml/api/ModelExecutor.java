package hu.elte.txtuml.api;

import java.io.PrintStream;

public final class ModelExecutor implements ModelElement {
	/*
	 * Accessed from multiple threads, so must be thread-safe.
	 */

	private static final ModelExecutorThread thread = new ModelExecutorThread();

	private ModelExecutor() {
	}

	// SETTINGS

	public static final class Settings implements ModelElement {

		private static volatile PrintStream userOutStream = System.out;
		private static volatile PrintStream userErrorStream = System.err;
		private static volatile PrintStream executorOutStream = System.out;
		private static volatile PrintStream executorErrorStream = System.err;
		private static volatile boolean executorLog = false;
		private static volatile boolean dynamicChecks = true;

		private static final Object LOCK_ON_EXECUTION_TIME_MULTIPLIER = new Object();
		private static long executionTimeMultiplier = 1;

		private static volatile boolean canChangeExecutionTimeMultiplier = true;

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

		public static void setDynamicChecks(boolean newValue) {
			Settings.dynamicChecks = newValue;
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
		thread.send(target, signal);
	}

	// LOGGING METHODS

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

	private static void logOnStream(PrintStream printStream, String message) {
		synchronized (printStream) {
			printStream.println(message);
		}
	}
}
