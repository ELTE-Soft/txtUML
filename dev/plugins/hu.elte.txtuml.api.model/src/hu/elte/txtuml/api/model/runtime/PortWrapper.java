package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Reception;
import hu.elte.txtuml.api.model.Signal;

/**
 * Wraps a port instance, providing additional runtime information and
 * management capabilities.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface PortWrapper extends Wrapper<Port<?, ?>>, RuntimeInfo {

	/**
	 * Called by {@link Action#send(Signal, Reception)}; asynchronously sends a
	 * signal to this port.
	 */
	void send(Signal signal);

}
