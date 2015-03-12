package txtuml.api.backend.collections;

import java.util.Map;

import txtuml.api.backend.collections.impl.InnerClassInstancesMapImpl;

public interface InnerClassInstancesMap extends Map<Class<?>, Object> {

	static InnerClassInstancesMap create() {
		return new InnerClassInstancesMapImpl();
	}

	@Override
	public String toString();

}
