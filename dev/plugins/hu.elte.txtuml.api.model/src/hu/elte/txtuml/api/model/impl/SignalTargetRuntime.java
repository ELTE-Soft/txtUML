package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;

/**
 * Wraps a signal target which is either a model class instance or a port
 * instance.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface SignalTargetRuntime<T> extends ModelElementRuntime<T>, ImplRelated {

	/**
	 * Asynchronously sends a signal to this object.
	 * <p>
	 * Thread-safe.
	 */
	void receiveLater(Signal signal);

	/**
	 * Asynchronously sends a signal to this object from the given sender.
	 * (Currently only used for {@linkplain ModelClass} targets, not for ports.)
	 * <p>
	 * Thread-safe.
	 */
	void receiveLater(Signal signal, ModelClass sender);

}
