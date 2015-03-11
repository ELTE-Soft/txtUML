package hu.elte.txtuml.api.backend.collections.impl;

import hu.elte.txtuml.api.backend.collections.InnerClassInstancesMap;

import java.util.HashMap;

@SuppressWarnings("serial")
public class InnerClassInstancesMapImpl extends HashMap<Class<?>, Object>
		implements InnerClassInstancesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}

}
