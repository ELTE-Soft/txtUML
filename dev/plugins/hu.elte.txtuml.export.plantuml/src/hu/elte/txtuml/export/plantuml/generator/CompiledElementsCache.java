package hu.elte.txtuml.export.plantuml.generator;

import java.util.HashMap;

public class CompiledElementsCache {

	private HashMap<String, String> compiledElements;

	public CompiledElementsCache() {
		compiledElements = new HashMap<String, String>();
	}

	public void addCompiledElement(String fullyQualifiedName, String element) {
		compiledElements.put(fullyQualifiedName, element);
	}

	public String getCompiledElement(String fullyQualifiedName) {
		return compiledElements.get(fullyQualifiedName);
	}

	public boolean hasCompiledElement(String fullyQualifiedName) {
		return compiledElements.containsKey(fullyQualifiedName);
	}
}
