package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Signal;

public interface SignalSenderWrapper<W> extends Wrapper<W>, RuntimeInfo {
	/**
	 * The object asynchronously sent a signal*
	 * <p>
	 * Thread-safe.
	 */
	void sent(Signal signal);
}
