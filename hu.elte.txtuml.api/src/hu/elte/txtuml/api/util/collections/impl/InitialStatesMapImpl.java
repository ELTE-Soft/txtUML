package hu.elte.txtuml.api.util.collections.impl;

import hu.elte.txtuml.api.ModelClass.InitialState;
import hu.elte.txtuml.api.util.collections.InitialStatesMap;

import java.util.HashMap;

@SuppressWarnings("serial")
public class InitialStatesMapImpl extends
		HashMap<Class<?>, Class<? extends InitialState>> implements
		InitialStatesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}

}
