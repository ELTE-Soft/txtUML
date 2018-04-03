package hu.elte.txtuml.api.model.execution;

/**
 * The different levels of dynamic checks performed during a model execution.
 */
public enum CheckLevel {
	// There are no "RECOMMENDED" checks at the moment, and the need for this
	// level is questionable. (Shouldn't a recommended check be mandatory?)
	// However, the use of this level is an option for the future.

	MANDATORY, /* RECOMMENDED, */ OPTIONAL;

	public boolean isAtLeast(CheckLevel other) {
		return ordinal() >= other.ordinal();
	}
}