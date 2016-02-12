package hu.elte.txtuml.export.cpp.templates;

public class Options {
	private static boolean addRuntime = false;

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
