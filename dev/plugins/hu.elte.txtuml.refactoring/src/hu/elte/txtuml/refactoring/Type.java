package hu.elte.txtuml.refactoring;

public enum Type {
	STATE("State"),
	COMPOSITE_STATE("CompositeState"),
	TRANSITION("Transition"),
	FROM_ANNOTATION("From"),
	TO_ANNOTATION("To");
	
	private String name;
	
	private Type(String name) {
		this.name = name;
	}
	
	public String get() {
		return name;
	}
}
