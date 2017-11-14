package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.runtime.SignalTargetWrapper;

/**
 * Abstract base class for {@link SignalTargetWrapper} implementations.
 */
public abstract class AbstractSignalTargetWrapper<W> extends BaseWrapper<W> implements SignalTargetWrapper<W> {

	public AbstractSignalTargetWrapper(W wrapped) {
		super(wrapped);
	}

	/**
	 * The runtime instance that owns this object.
	 * <p>
	 * This implementation is only a shorthand for {@link #getThread()}.
	 * {@link ModelExecutorThread#getRuntime() getRuntime()}.
	 */
	@Override
	public AbstractRuntime<?, ?> getRuntime() {
		return getThread().getRuntime();
	}

	/**
	 * The model executor thread that runs this object.
	 */
	public abstract ModelExecutorThread getThread();

	/**
	 * This target receives the specified signal.
	 */
	public abstract void receive(Signal signal);

	/**
	 * This target receives the specified signal. If the signal goes through a
	 * chain of ports, {@code sender} is last port in that chain before this
	 * target.
	 */
	public abstract void receive(Signal signal, AbstractPortWrapper sender);

	/**
	 * Asynchronously sends the given signal to this target from the specified
	 * sender. If the signal goes through a chain of ports, {@code sender} is
	 * last port in that chain before this target.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void send(Signal signal, AbstractPortWrapper sender);

}
