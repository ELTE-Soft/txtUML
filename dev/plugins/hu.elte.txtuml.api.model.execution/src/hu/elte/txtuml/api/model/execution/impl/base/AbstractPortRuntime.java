package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.impl.PortRuntime;
import hu.elte.txtuml.api.model.Signal;

/**
 * Abstract base class for {@link PortRuntime} implementations.
 */
public abstract class AbstractPortRuntime extends AbstractSignalTargetRuntime<Port<?, ?>> implements PortRuntime {

	private final AbstractModelClassRuntime owner;

	public AbstractPortRuntime(Port<?, ?> wrapped, AbstractModelClassRuntime owner) {
		super(wrapped);
		this.owner = owner;
	}

	@Override
	public void receiveLater(Signal signal, AbstractPortRuntime sender) {
		getThread().receiveLater(signal, this, sender);
	}

	@Override
	public String getStringRepresentation() {
		return owner.getStringRepresentation() + ":" + getTypeOfWrapped().getSimpleName();
	}

	@Override
	public AbstractExecutorThread getThread() {
		return owner.getThread();
	}

	/**
	 * Gets the owner of the wrapped port instance.
	 */
	public AbstractModelClassRuntime getOwner() {
		return owner;
	}

	/**
	 * Sends the given signal to the given target unless target is null. In the
	 * latter case, a runtime warning is shown about a lost signal at this port.
	 */
	protected void tryToSend(Signal signal, AbstractSignalTargetRuntime<?> target) {
		if (target != null) {
			target.receiveLater(signal, this);
			return;
		}
		getModelRuntime().warning(x -> x.lostSignalAtPort(signal, getWrapped()));
	}

	/**
	 * Gets the 'outer' connection of the wrapped port which is a target outside
	 * the owner class.
	 */
	public abstract AbstractSignalTargetRuntime<?> getOuterConnection();

	/**
	 * Gets the 'inner' connection of the wrapped port which is a target inside
	 * the owner class.
	 */
	public abstract AbstractSignalTargetRuntime<?> getInnerConnection();

	/**
	 * Sets the 'outer' connection of the wrapped port which must be a target
	 * outside the owner class.
	 */
	public abstract void setOuterConnection(AbstractSignalTargetRuntime<?> other);

	/**
	 * Sets the 'inner' connection of the wrapped port which must be a target
	 * inside the owner class.
	 */
	public abstract void setInnerConnection(AbstractSignalTargetRuntime<?> other);

	@Override
	public abstract void receive(Signal signal, AbstractPortRuntime sender);

}
