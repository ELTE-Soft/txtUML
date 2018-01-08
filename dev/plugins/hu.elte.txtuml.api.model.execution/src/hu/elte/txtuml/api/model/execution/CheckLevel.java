package hu.elte.txtuml.api.model.execution;

/**
 * The different levels of dynamic checks performed during a model
 * execution.
 */
public enum CheckLevel {
	MANDATORY, /* RECOMMENDED, */ OPTIONAL;

	public boolean isAtLeast(CheckLevel other) {
		return ordinal() >= other.ordinal();
	}
}