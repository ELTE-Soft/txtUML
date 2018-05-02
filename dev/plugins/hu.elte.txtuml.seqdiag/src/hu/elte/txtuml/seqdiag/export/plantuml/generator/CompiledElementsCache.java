package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import java.util.HashMap;
import java.util.List;

/**
 * Cache element for the {@link CompileCache}
 */
public class CompiledElementsCache {

	private HashMap<String, CompileCache> compiledElements;

	public CompiledElementsCache() {
		compiledElements = new HashMap<String, CompileCache>();
	}

	public void addCompiledElement(String fullyQualifiedName, String element, List<String> activatedLifelines) {
		compiledElements.put(fullyQualifiedName, new CompileCache(element, activatedLifelines));
	}

	public CompileCache getCompiledElement(String fullyQualifiedName) {
		return compiledElements.get(fullyQualifiedName);
	}

	public boolean hasCompiledElement(String fullyQualifiedName) {
		return compiledElements.containsKey(fullyQualifiedName);
	}
}
