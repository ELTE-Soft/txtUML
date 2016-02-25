package hu.elte.txtuml.api.model.runtime;

public interface RuntimeInfo extends RuntimeContext {

	/**
	 * Returns a short string representation of the described object.
	 */
	String getStringRepresentation();

	/**
	 * Returns a detailed string representation of the described object.
	 */
	@Override
	String toString();

}
