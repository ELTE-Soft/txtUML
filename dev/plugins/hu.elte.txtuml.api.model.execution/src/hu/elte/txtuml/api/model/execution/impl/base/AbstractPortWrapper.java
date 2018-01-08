package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.impl.PortWrapper;
import hu.elte.txtuml.api.model.Signal;

/**
 * Abstract base class for {@link PortWrapper} implementations.
 */
public abstract class AbstractPortWrapper extends AbstractSignalTargetWrapper<Port<?, ?>> implements PortWrapper {

	private final AbstractModelClassWrapper owner;

	public AbstractPortWrapper(Port<?, ?> wrapped, AbstractModelClassWrapper owner) {
		super(wrapped);
		this.owner = owner;
	}

	@Override
	public void send(Signal signal) {
		getThread().send(signal, this);
	}

	@Override
	public void send(Signal signal, AbstractPortWrapper sender) {
		getThread().send(signal, sender, this);
	}

	@Override
	public ModelExecutorThread getThread() {
		return getOwner().getThread();
	}

	@Override
	public String getStringRepresentation() {
		return owner.getStringRepresentation() + ":" + getTypeOfWrapped().getSimpleName();
	}

	/**
	 * Gets the owner of the wrapped port instance.
	 */
	public AbstractModelClassWrapper getOwner() {
		return owner;
	}

	/**
	 * Sends the given signal to the given target unless target is null. In the
	 * latter case, a runtime warning is shown about a lost signal at this port.
	 */
	protected void tryToSend(Signal signal, AbstractSignalTargetWrapper<?> target) {
		if (target != null) {
			target.send(signal, this);
			return;
		}
		getRuntime().warning(x -> x.lostSignalAtPort(signal, getWrapped()));
	}

	/**
	 * Gets the 'outer' connection of the wrapped port which is a target outside
	 * the owner class.
	 */
	public abstract AbstractSignalTargetWrapper<?> getOuterConnection();

	/**
	 * Gets the 'inner' connection of the wrapped port which is a target inside
	 * the owner class.
	 */
	public abstract AbstractSignalTargetWrapper<?> getInnerConnection();

	/**
	 * Sets the 'outer' connection of the wrapped port which must be a target
	 * outside the owner class.
	 */
	public abstract void setOuterConnection(AbstractSignalTargetWrapper<?> other);

	/**
	 * Sets the 'inner' connection of the wrapped port which must be a target
	 * inside the owner class.
	 */
	public abstract void setInnerConnection(AbstractSignalTargetWrapper<?> other);

	@Override
	public abstract void receive(Signal signal);

	@Override
	public abstract void receive(Signal signal, AbstractPortWrapper sender);

}
