package hu.elte.txtuml.api.util.collections;

import hu.elte.txtuml.api.util.collections.impl.InnerClassInstancesMapImpl;

import java.util.Map;

public interface InnerClassInstancesMap extends Map<Class<?>, Object> {

	static InnerClassInstancesMap create() {
		return new InnerClassInstancesMapImpl();
	}

	@Override
	public String toString();

}
