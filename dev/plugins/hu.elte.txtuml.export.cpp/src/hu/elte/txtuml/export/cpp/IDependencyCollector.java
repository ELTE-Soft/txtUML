package hu.elte.txtuml.export.cpp;

public interface IDependencyCollector {
	void addDependency(String type);
	void addCppOnlyDependency(String type);
}
