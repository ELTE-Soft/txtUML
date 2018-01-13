package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.LowerBoundError;

/**
 * Wraps a model class instance, providing additional runtime information and
 * management capabilities.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface ModelClassRuntime extends SignalTargetRuntime<ModelClass>, ImplRelated {

	/**
	 * The unique identifier of the wrapped model class instance.
	 * <p>
	 * Thread-safe.
	 */
	String getIdentifier();

	/**
	 * The modifiable name of the wrapped model class instance.
	 * <p>
	 * Thread-safe.
	 */
	String getName();

	/**
	 * Sets the modifiable name of the wrapped model class instance.
	 * <p>
	 * Thread-safe.
	 */
	void setName(String name);

	/**
	 * The current status of the wrapped model class instance.
	 * <p>
	 * Thread-safe.
	 */
	Status getStatus();

	/**
	 * Called by {@link Action#start(ModelClass)}.
	 */
	void start();

	/**
	 * Called by {@link Action#delete(ModelClass)}.
	 */
	void delete();

	/**
	 * Called by {@link API#send}.
	  */
	void receiveLaterViaAPI(Signal signal);

	<T extends ModelClass, C extends GeneralCollection<T>, AE extends AssociationEnd<C> & Navigable> C navigateThroughAssociation(
			Class<AE> otherEnd) throws LowerBoundError;

	<P extends Port<?, ?>> P getPortInstance(Class<P> portType);

	<S extends Signal> S getCurrentTrigger();

}
