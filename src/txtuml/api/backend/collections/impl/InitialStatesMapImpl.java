package txtuml.api.backend.collections.impl;

import java.util.HashMap;

import txtuml.api.StateMachine.InitialState;
import txtuml.api.backend.collections.InitialStatesMap;

@SuppressWarnings("serial")
public class InitialStatesMapImpl extends
		HashMap<Class<?>, Class<? extends InitialState>> implements
		InitialStatesMap {

	@Override
	public String toString() {
		return "NO STRING REPRESENTATION [field is only for private use of the txtuml model execution]";
	}

}
