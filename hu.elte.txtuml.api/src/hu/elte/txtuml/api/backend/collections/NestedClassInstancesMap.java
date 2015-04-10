package hu.elte.txtuml.api.backend.collections;

import hu.elte.txtuml.api.backend.collections.impl.NestedClassInstancesMapImpl;

import java.util.Map;

public interface NestedClassInstancesMap extends Map<Class<?>, Object> {

	static NestedClassInstancesMap create() {
		return new NestedClassInstancesMapImpl();
	}

	@Override
	public String toString();

}
