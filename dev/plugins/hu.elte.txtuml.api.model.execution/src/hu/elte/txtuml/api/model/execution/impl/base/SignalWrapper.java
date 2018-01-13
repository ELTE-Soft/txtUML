package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.util.WrapperImpl;

/**
 * Wraps a nullable signal instance, also containing a reference to the original
 * sender of the signal (if known) and the last post instance in the chain of
 * ports through which the signal arrives to its current target (if arriving via
 * ports).
 */
public class SignalWrapper extends WrapperImpl<Signal> {
	private final ModelClass sender;
	private final AbstractPortRuntime port;

	public static SignalWrapper of(Signal signal) {
		return new SignalWrapper(signal, null, null);
	}

	public static SignalWrapper of(Signal signal, ModelClass sender) {
		return new SignalWrapper(signal, sender, null);
	}

	public static SignalWrapper viaPort(SignalWrapper signal, AbstractPortRuntime port) {
		return new SignalWrapper(signal.getWrapped(), signal.getSenderOrNull(), port);
	}

	protected SignalWrapper(Signal signal, ModelClass sender, AbstractPortRuntime lastPort) {
		super(signal);
		this.sender = sender;
		this.port = lastPort;
	}

	/**
	 * The {@code sender} is the object who originally sent this signal, if
	 * known.
	 */
	public boolean isSenderKnown() {
		return sender != null;
	}

	/**
	 * The {@code sender} is the object who originally sent this signal, if
	 * known.
	 */
	public ModelClass getSenderOrNull() {
		return sender;
	}

	/**
	 * Whether the signal goes through a chain of ports.
	 */
	public boolean isViaPort() {
		return port != null;
	}

	/**
	 * If the signal goes through a chain of ports, {@code port} is the last
	 * port in that chain before the current target.
	 */
	public AbstractPortRuntime getPortOrNull() {
		return port;
	}

	/**
	 * If the signal goes through a chain of ports, {@code port} is the last
	 * port in that chain before the current target.
	 */
	public Port<?, ?> getRawPortOrNull() {
		return port == null ? null : port.getWrapped();
	}

	/**
	 * If contains no signal. (Empty signals are used to step forward from
	 * initial pseudostates.)
	 */
	public boolean isEmpty() {
		return getWrapped() == null;
	}

}
