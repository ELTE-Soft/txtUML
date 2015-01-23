package txtuml.api;

public abstract interface ModelElement {}
// it is needed for AspectJ to work well
// all classes that are used in the model should implement this interface (not directly but through inheritance)