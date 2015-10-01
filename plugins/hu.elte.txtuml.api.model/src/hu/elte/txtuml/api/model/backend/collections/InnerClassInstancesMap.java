package hu.elte.txtuml.api.model.backend.collections;

import hu.elte.txtuml.api.model.backend.collections.impl.InnerClassInstancesMapImpl;

import java.util.Map;

/**
 * A mapping used in the <code>InnerClassInstancesHolder</code> package private
 * class in the {@link hu.elte.txtuml.api} package. It maps classes to their
 * single instances.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface InnerClassInstancesMap extends Map<Class<?>, Object> {

	/**
	 * Creates a new <code>InnerClassInstancesMap</code> instance.
	 * 
	 * @return the new instance
	 */
	static InnerClassInstancesMap create() {
		return new InnerClassInstancesMapImpl();
	}

	@Override
	public String toString();

}
