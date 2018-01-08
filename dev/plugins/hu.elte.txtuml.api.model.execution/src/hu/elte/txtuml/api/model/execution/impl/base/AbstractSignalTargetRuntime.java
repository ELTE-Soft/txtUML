package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.util.WrapperImpl;
import hu.elte.txtuml.api.model.impl.SignalTargetRuntime;

/**
 * Abstract base class for {@link SignalTargetRuntime} implementations.
 */
public abstract class AbstractSignalTargetRuntime<W> extends WrapperImpl<W> implements SignalTargetRuntime<W> {

	public AbstractSignalTargetRuntime(W wrapped) {
		super(wrapped);
	}

	/**
	 * The runtime instance that owns this object.
	 * <p>
	 * This implementation is only a shorthand for {@link #getThread()}.
	 * {@link ModelExecutorThread#getRuntime() getRuntime()}.
	 */
	@Override
	public AbstractModelRuntime<?, ?> getModelRuntime() {
		return getThread().getModelRuntime();
	}

	/**
	 * The model executor thread that runs this object.
	 */
	@Override
	public abstract AbstractExecutorThread getThread();

	/**
	 * This target receives the specified signal. If the signal goes through a
	 * chain of ports, {@code sender} is last port in that chain before this
	 * target; otherwise it is {@code null}.
	 * <p>
	 * This method is <b>not</b> thread-safe. Should only be called from the
	 * owner thread.
	 */
	public abstract void receive(Signal signal, AbstractPortRuntime sender);

	@Override
	public void receiveLater(Signal signal) {
		receiveLater(signal, null);
	}

	/**
	 * Asynchronously sends the given signal to this target from the specified
	 * sender. If the signal goes through a chain of ports, {@code sender} is
	 * last port in that chain before this target; otherwise it is {@code null}.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void receiveLater(Signal signal, AbstractPortRuntime sender);

}
