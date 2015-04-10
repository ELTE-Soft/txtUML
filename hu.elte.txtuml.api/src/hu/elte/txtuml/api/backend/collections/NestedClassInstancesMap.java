package hu.elte.txtuml.api.backend.collections;

import hu.elte.txtuml.api.backend.collections.impl.NestedClassInstancesMapImpl;

import java.util.Map;

/**
 * A mapping used in the <code>NestedClassInstancesHolder</code> package private
 * class in the {@link hu.elte.txtuml.api} package. It maps classes to their
 * single instances.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface NestedClassInstancesMap extends Map<Class<?>, Object> {

	/**
	 * Creates a new <code>NestedClassInstancesMap</code> instance.
	 * 
	 * @return the new instance
	 */
	static NestedClassInstancesMap create() {
		return new NestedClassInstancesMapImpl();
	}

	/*
	 * The toString() method of NestedClassInstancesMap should be overridden.
	 */
	@Override
	public String toString();

}
