package hu.elte.txtuml.api.model.seqdiag;

/**
 * A runtime info describes a model element at runtime, providing additional
 * information about it.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
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
