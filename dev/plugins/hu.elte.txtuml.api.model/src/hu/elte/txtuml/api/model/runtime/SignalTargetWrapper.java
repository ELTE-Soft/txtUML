package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Signal;

/**
 * Wraps a signal target which is either a model class instance or a port
 * instance.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface SignalTargetWrapper<W> extends Wrapper<W>, RuntimeInfo {

	/**
	 * Asynchronously sends a signal to this object. *
	 * <p>
	 * Thread-safe.
	 */
	void send(Signal signal);

}
