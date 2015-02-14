package txtuml.api;

import java.io.PrintStream;

public final class Runtime implements ModelElement {
	private Runtime() {
	}

	public static final class Settings implements ModelElement {
		private static PrintStream userOutStream = System.out;
		private static PrintStream userErrorStream = System.err;
		private static PrintStream runtimeOutStream = System.out;
		private static PrintStream runtimeErrorStream = System.err;
		private static long simulationTimeMultiplier = 1;
		private static boolean canChangeSimulationTimeMultiplier = true;
		private static boolean runtimeLog = false;

		private Settings() {
		}

		public static void setUserOutStream(PrintStream userOutStream) {
			Settings.userOutStream = userOutStream;
		}

		public static void setUserErrorStream(PrintStream userErrorStream) {
			Settings.userErrorStream = userErrorStream;
		}

		public static void setRuntimeOutStream(PrintStream runtimeOutStream) {
			Settings.runtimeOutStream = runtimeOutStream;
		}

		public static void setRuntimeErrorStream(PrintStream runtimeErrorStream) {
			Settings.runtimeErrorStream = runtimeErrorStream;
		}

		public static void setRuntimeLog(boolean newValue) {
			runtimeLog = newValue;
		}

		public static void setSimulationTimeMultiplier(long newMultiplier) {
			if (canChangeSimulationTimeMultiplier) {
				simulationTimeMultiplier = newMultiplier;
			} else {
				Action.runtimeErrorLog("Error: Simulation time multiplier can only be changed before any time-related event takes place in the model simulation");
			}
		}

		public static long getSimulationTimeMultiplier() {
			return simulationTimeMultiplier;
		}

		public static void lockSimulationTimeMultiplier() {
			canChangeSimulationTimeMultiplier = false;
		}

		static boolean runtimeLog() {
			return runtimeLog;
		}
	}

	static void log(String message) { // user log
		synchronized (Settings.userOutStream) {
			Settings.userOutStream.println(message);
		}
	}

	static void logError(String message) { // user log
		synchronized (Settings.userErrorStream) {
			Settings.userErrorStream.println(message);
		}
	}

	static void runtimeLog(String message) { // api log
		synchronized (Settings.runtimeOutStream) {
			Settings.runtimeOutStream.println(message);
		}
	}

	static void runtimeFormattedLog(String format, Object... args) { // api log
		synchronized (Settings.runtimeOutStream) {
			Settings.runtimeOutStream.format(format, args);
		}
	}

	static void runtimeErrorLog(String message) { // api log
		synchronized (Settings.runtimeErrorStream) {
			Settings.runtimeErrorStream.println(message);
		}
	}

	static void send(ModelClass receiverObj, Signal event) {
		receiverObj.send(event);
	}
}
