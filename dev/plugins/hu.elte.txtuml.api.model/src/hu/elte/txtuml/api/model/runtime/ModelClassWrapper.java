package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.assocends.Navigability;

public interface ModelClassWrapper extends Wrapper<ModelClass>, RuntimeInfo {

	/**
	 * The unique identifier of the wrapped model class instance.
	 */
	String getIdentifier();

	/**
	 * The modifiable name of the wrapped model class instance.
	 */
	String getName();

	/**
	 * Sets the modifiable name of the wrapped model class instance.
	 */
	void setName(String name);

	/**
	 * Called by {@link Action#start(ModelClass)}.
	 */
	void start();

	/**
	 * Called by {@link Action#delete(ModelClass)}.
	 */
	void delete();

	/**
	 * Called by {@link Action#send(Signal, ModelClass)}.
	 */
	void send(Signal signal);

	/**
	 * The current status of the wrapped model class instance.
	 */
	Status getStatus();

	<T extends ModelClass, C extends Collection<T>, AE extends AssociationEnd<T, C> & Navigability.Navigable> C navigateThroughAssociation(
			Class<AE> otherEnd);

	<P extends Port<?, ?>> P getPortInstance(Class<P> portType);

	<S extends Signal> S getCurrentTriggeringSignal();

}
