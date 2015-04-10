package hu.elte.txtuml.api.backend.collections.impl;

import hu.elte.txtuml.api.backend.collections.NestedClassInstancesMap;

import java.util.HashMap;

@SuppressWarnings("serial")
public class NestedClassInstancesMapImpl extends HashMap<Class<?>, Object>
		implements NestedClassInstancesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}

}
