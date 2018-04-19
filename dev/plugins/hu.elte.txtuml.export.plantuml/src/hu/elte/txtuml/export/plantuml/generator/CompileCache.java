package hu.elte.txtuml.export.plantuml.generator;

import java.util.List;

/**
 * Compilation cache for Combined fragment compiler.
 */
public class CompileCache {
	private String compiledOutput;
	private List<String> activatedLifelines;

	public CompileCache(String compiledOutput, List<String> activatedLifelines) {
		this.compiledOutput = compiledOutput;
		this.activatedLifelines = activatedLifelines;
	}

	public String getCompiledOutput() {
		return this.compiledOutput;
	}

	public List<String> getActiveLifeines() {
		return this.activatedLifelines;
	}
}
