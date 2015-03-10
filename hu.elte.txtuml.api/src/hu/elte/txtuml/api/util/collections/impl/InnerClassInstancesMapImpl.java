package hu.elte.txtuml.api.util.collections.impl;

import java.util.HashMap;

import hu.elte.txtuml.api.util.collections.InnerClassInstancesMap;

@SuppressWarnings("serial")
public class InnerClassInstancesMapImpl extends HashMap<Class<?>, Object> implements InnerClassInstancesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}
	
}
