package hu.elte.txtuml.export.cpp.templates;

public class Options {
	private static boolean debugLog = false;
	private static boolean addRuntime = false;
	private static boolean threadManagement = false;

	public static void setDebugLog() {
		setDebugLog(true);
	}

	public static void setDebugLog(boolean value_) {
		debugLog = value_;
	}

	public static boolean isDebugLog() {
		return debugLog;
	}

	public static void setRuntime() {
		setAddRuntime(true);
	}

	public static void setAddRuntime(boolean value_) {
		addRuntime = value_;
	}

	public static boolean isAddRuntime() {
		return addRuntime;
	}

}
