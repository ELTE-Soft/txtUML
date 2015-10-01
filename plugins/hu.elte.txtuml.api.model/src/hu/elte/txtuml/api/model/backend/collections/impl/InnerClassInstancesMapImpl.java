package hu.elte.txtuml.api.model.backend.collections.impl;

import hu.elte.txtuml.api.model.backend.collections.InnerClassInstancesMap;

import java.util.HashMap;

/**
 * Default implementation for {@link InnerClassInstancesMap}.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link HashMap}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 */
@SuppressWarnings("serial")
public class InnerClassInstancesMapImpl extends HashMap<Class<?>, Object>
		implements InnerClassInstancesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}

}
