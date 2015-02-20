package txtuml.api;

import java.io.PrintStream;

// WARNING: file is in a completely temporary state.

// currently singleton, will have more instances in the future

public final class ModelExecutor<MODEL extends Model> implements ModelElement {
	/*
	 * This class has to be thread-safe as every component will communicate with
	 * it.
	 */

	private static final Object LOCK = new Object(); // TODO change to non-static when possible
	private static ModelExecutorThread thread = null; // TODO erase when possible
	
	private final Settings settings = new Settings();
	//private final MODEL model;
	//private final Configuration<MODEL> configuration;
	private ModelExecutorThread.Group executorGroup = null;

	/*
	public ModelExecutor(Class<MODEL> modelClass,
			Configuration<MODEL> configuration) {
		
		if (modelClass == null || configuration == null) {
			throw new IllegalArgumentException(
					"ModelExecutor instance cannot be created with null parameters.");
		}
		this.model = InstanceCreator.createInstanceWithGivenParams(modelClass); // null
																				// parameter
																				// constructor
																				// is
																				// called
		if (this.model == null) {
			throw new IllegalArgumentException(
					"Instantiation of given modelClass type failed: given type cannot be instantiated with null parameter constructor.");
		}
		this.configuration = configuration;
	}
	*/
	
	private ModelExecutor() { // TODO erase when possible
	}

	static ModelExecutorThread getExecutorThread() {
		/*
		try {
			return ((ModelExecutorThread) Thread.currentThread());
		} catch (ClassCastException ex) {
			System.err
					.println("Error: model commands can only be executed from within the context of a model and run by an instance of ModelExecutor class.");
			throw ex;
		}
		*/
		synchronized (LOCK) {
			if (thread == null) {
				thread = new ModelExecutorThread(new ModelExecutorThread.Group(new ModelExecutor<Model>()));
				thread.start();
			}
			return thread;
		}
	}
	
	static ModelExecutor<?> getExecutor() {
		return getExecutorThread().getExecutor();
	}

	Settings getSettings() {
		return settings;
	}

	static Settings getSettingsStatic() {
		return getExecutor().settings;
	}

	// SETTINGS

	public static final class Settings implements ModelElement {
		/*
		 * In the setters of the four streams, no synchronization is needed
		 * because the assignment is atomic and if a printing operation is
		 * active on another thread, that operation will completely finish on
		 * the old stream in either way.
		 */

		private PrintStream userOutStream = System.out;
		private PrintStream userErrorStream = System.err;
		private PrintStream executorOutStream = System.out;
		private PrintStream executorErrorStream = System.err;
		private long simulationTimeMultiplier = 1;
		private boolean canChangeSimulationTimeMultiplier = true;
		private boolean executorLog = false;

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

		@SuppressWarnings("static-access")
		public static void setExecutorLog(boolean newValue) {
			ModelExecutor<?> executor = getExecutor();
			synchronized (executor.LOCK) {
				executor.getSettings().executorLog = newValue;
			}
		}

		@SuppressWarnings("static-access")
		public static void setSimulationTimeMultiplier(long newMultiplier) {
			ModelExecutor<?> executor = getExecutor();
			synchronized (executor.LOCK) {
				Settings settings = executor.getSettings();
				if (settings.canChangeSimulationTimeMultiplier) {
					settings.simulationTimeMultiplier = newMultiplier;
				} else {
					Action.executorErrorLog("Error: Simulation time multiplier can only be changed before any time-related event has taken place in the model simulation.");
				}
			}
		}

		public static long getSimulationTimeMultiplier() {
			return getSettingsStatic().simulationTimeMultiplier;
		}

		@SuppressWarnings("static-access")
		public static void lockSimulationTimeMultiplier() {
			ModelExecutor<?> executor = getExecutor();
			synchronized (executor.LOCK) {
				executor.getSettings().canChangeSimulationTimeMultiplier = false;
			}
		}

		boolean executorLog() {
			return executorLog;
		}

		static boolean executorLogStatic() {
			return getSettingsStatic().executorLog;
		}
	}

	// START EXECUTION

	/*public*/ void start() {
		if (executorGroup != null) {
			System.err
					.println("A ModelExecutor instance can only perform one execution at a time.");
			return;
		}
		executorGroup = new ModelExecutorThread.Group(this);

		new MainModelExecutorThread(executorGroup).start();
	}

	// MAIN THREAD

	private class MainModelExecutorThread extends ModelExecutorThread {

		MainModelExecutorThread(Group group) {
			super(group);
		}

		/*
		@Override
		public void run() {
			configuration.configure(model);
			super.run();
		}
		*/
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
