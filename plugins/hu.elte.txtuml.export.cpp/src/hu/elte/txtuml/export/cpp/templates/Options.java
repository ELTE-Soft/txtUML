package hu.elte.txtuml.export.cpp.templates;

public class Options {
	private final boolean addRuntime;

	public Options(boolean addRuntime_) {
		this.addRuntime = addRuntime_;
	}

	public boolean isAddRuntime() {
		return addRuntime;
	}

}
