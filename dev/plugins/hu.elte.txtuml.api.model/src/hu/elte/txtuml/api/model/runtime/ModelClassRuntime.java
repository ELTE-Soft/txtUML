package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.error.LowerBoundError;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ImplRelated;

/**
 * Wraps a model class instance, providing additional runtime information and
 * management capabilities.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.runtime} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface ModelClassRuntime extends SignalTargetRuntime<ModelClass>, ImplRelated {

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
	 * The current status of the wrapped model class instance.
	 */
	Status getStatus();

	/**
	 * The object asynchronously sent a signal.
	 * <p>
	 * Thread-safe.
	 */
	void didSend(Signal signal);

	<T extends ModelClass, C extends GeneralCollection<T>, AE extends AssociationEnd<C> & Navigable> C navigateThroughAssociation(
			Class<AE> otherEnd) throws LowerBoundError;

	<P extends Port<?, ?>> P getPortInstance(Class<P> portType);

	<S extends Signal> S getCurrentTrigger();

}
