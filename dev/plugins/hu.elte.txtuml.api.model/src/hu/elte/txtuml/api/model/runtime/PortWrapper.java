package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Reception;
import hu.elte.txtuml.api.model.Signal;

public interface PortWrapper extends Wrapper<Port<?, ?>>, RuntimeInfo {

	/**
	 * Called by {@link Action#send(Signal, Reception)}.
	 */
	<S extends Signal> void send(S signal);

}
