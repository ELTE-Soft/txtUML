package hu.elte.txtuml.api.model.execution;

/**
 * The different levels of model execution logs.
 */
public enum LogLevel {
	ERROR, WARNING, TRACE;

	public boolean isAtLeast(LogLevel other) {
		return ordinal() >= other.ordinal();
	}
}