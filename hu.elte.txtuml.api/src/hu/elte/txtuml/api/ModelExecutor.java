package hu.elte.txtuml.api;

import java.io.PrintStream;

// TODO check

/*
 * Currently not instantiatable, will have more instances in the future.
 */
public final class ModelExecutor implements ModelElement {
	/*
	 * Accessed from multiple threads, so must be thread-safe.
	 */

	private ModelExecutor() {
	}

	static ModelExecutorThread getExecutorThreadStatic() {
		return ModelExecutorThread.getSingletonInstance();
	}

	static Settings getSettingsStatic() {
		return Settings.instance;
	}

	// SETTINGS

	/*
	 * Currently singleton.
	 */
	public static final class Settings implements ModelElement {
		/*
		 * In the setters of the four streams, no synchronization is needed
		 * because the assignment is atomic and if a printing operation is
		 * active on another thread, that operation will completely finish on
		 * the old stream in either way.
		 */

		private static final Settings instance = new Settings();

		private PrintStream userOutStream = System.out;
		private PrintStream userErrorStream = System.err;
		private PrintStream executorOutStream = System.out;
		private PrintStream executorErrorStream = System.err;
		private boolean executorLog = false;

		private final Object lockOnExecutionTimeMultiplier = new Object();
		private long executionTimeMultiplier = 1;
		private boolean canChangeExecutionTimeMultiplier = true;

		private Settings() {
		}

		public static void setUserOutStream(PrintStream userOutStream) {
			getSettingsStatic().userOutStream = userOutStream;
		}

		public static void setUserErrorStream(PrintStream userErrorStream) {
			getSettingsStatic().userErrorStream = userErrorStream;
		}

		public static void setExecutorOutStream(PrintStream executorOutStream) {
			getSettingsStatic().executorOutStream = executorOutStream;
		}

		public static void setExecutorErrorStream(
				PrintStream executorErrorStream) {
			getSettingsStatic().executorErrorStream = executorErrorStream;
		}

		public static void setExecutorLog(boolean newValue) {
			getSettingsStatic().executorLog = newValue;
		}

		public static void setExecutionTimeMultiplier(long newMultiplier) {
			Settings settings = getSettingsStatic();
			synchronized (settings.lockOnExecutionTimeMultiplier) {
				if (settings.canChangeExecutionTimeMultiplier) {
					settings.executionTimeMultiplier = newMultiplier;
				} else {
					// TODO show error
				}
			}
		}

		public static long getExecutionTimeMultiplier() {
			/*
			 * Reading a long value is not atomic, so synchronization is needed.
			 */
			synchronized (getSettingsStatic().lockOnExecutionTimeMultiplier) {
				return getSettingsStatic().executionTimeMultiplier;
			}
		}

		public static void lockExecutionTimeMultiplier() {
			getSettingsStatic().canChangeExecutionTimeMultiplier = false;
		}

		static boolean executorLog() {
			return getSettingsStatic().executorLog;
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

	static void log(String message) { // user log
		logOnStream(getSettingsStatic().userOutStream, message);
	}

	static void logError(String message) { // user log
		logOnStream(getSettingsStatic().userErrorStream, message);
	}

	static void executorLog(String message) { // api log
		logOnStream(getSettingsStatic().executorOutStream, message);
	}

	static void executorFormattedLog(String format, Object... args) { // api
																		// log
		PrintStream printStream = getSettingsStatic().executorOutStream;
		synchronized (printStream) {
			printStream.format(format, args);
		}
	}

	static void executorErrorLog(String message) { // api log
		logOnStream(getSettingsStatic().executorErrorStream, message);
	}
}
