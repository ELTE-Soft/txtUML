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

	// public boolean isSentByAPI() {
	// return target == null;
	// }

	// @Override
	// public boolean equals(Object other) {
	// if (other == this) {
	// return true;
	// }
	// if (!(other instanceof SignalWrapper)) {
	// return false;
	// } else {
	//
	// SignalWrapper otherWrapper = (SignalWrapper) other;
	//
	// if (otherWrapper.isSentByAPI() != this.isSentByAPI()) {
	// return false;
	// }
	//
	// if
	// (!getIdentifierOf(otherWrapper.receiver).equals(getIdentifierOf(receiver)))
	// {
	// return false;
	// }
	//
	// if (!signalsEqual(getWrapped(), otherWrapper.getWrapped())) {
	// return false;
	// }
	//
	// if (!isSentByAPI() &&
	// !getIdentifierOf(otherWrapper.target).equals(getIdentifierOf(target))) {
	// return false;
	// }
	//
	// }
	//
	// return true;
	// }
	//
	// private static boolean signalsEqual(Signal sig1, Signal sig2) {
	// if (sig1 == sig2) {
	// return true;
	// }
	// if (sig1 == null || sig2 == null) { // They are not the same.
	// return false;
	// }
	// if (sig1.getClass() != sig2.getClass()) {
	// return false;
	// }
	// for (Field[] array : getAllFields(sig1)) {
	// for (Field f : array) {
	// f.setAccessible(true);
	// try {
	// if (!Objects.equals(f.get(sig1), f.get(sig2))) {
	// return false;
	// }
	// } catch (IllegalAccessException e) {
	// Logger.sys.fatal("Signal field cannot be accessed", e);
	// }
	// }
	// }
	// return true;
	// }
	//
	// private static List<Field[]> getAllFields(Signal sig) {
	// List<Field[]> fields = new ArrayList<>();
	// for (Class<?> cls = sig.getClass(); cls != DataType.class; cls =
	// cls.getSuperclass()) {
	// fields.add(cls.getDeclaredFields());
	// }
	// return fields;
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 10009;
	// int result = getIdentifierOf(receiver).hashCode();
	// result = result * prime + (target == null ? 0 :
	// getIdentifierOf(target).hashCode());
	// result = result * prime + getTypeOfWrapped().hashCode();
	// return result;
	// }

}
