package hu.elte.txtuml.export.cpp.templates;

public class Options {
	private final boolean addRuntime;
	private final boolean overWriteMainFile;

	public Options(boolean addRuntime, boolean overWriteMainFile) {
		this.addRuntime = addRuntime;
		this.overWriteMainFile = overWriteMainFile;
	}

	public boolean isAddRuntime() {
		return addRuntime;
	}
	
	public boolean isOverWriteMainFile() {
		return overWriteMainFile;
	}

}
