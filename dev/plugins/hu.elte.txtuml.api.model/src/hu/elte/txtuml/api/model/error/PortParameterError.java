package hu.elte.txtuml.api.model.error;

/**
 * Thrown if a port is to be instantiated but its type parameters cannot be
 * retrieved.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class PortParameterError extends Error {

	public PortParameterError(Class<?> type) {
		super("The type parameters of port " + type.getName() + " cannot be retrieved.");
	}

}
