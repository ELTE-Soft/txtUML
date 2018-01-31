package hu.elte.txtuml.export.cpp;

public interface ICppCompilationUnit {

	String getUnitName();
	void addDependency(String type);
	void addCppOnlyDependency(String type);
	 

}
