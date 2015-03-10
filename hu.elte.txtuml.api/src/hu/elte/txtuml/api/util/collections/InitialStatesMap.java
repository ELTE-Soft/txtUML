package hu.elte.txtuml.api.util.collections;

import hu.elte.txtuml.api.ModelClass.InitialState;
import hu.elte.txtuml.api.util.collections.impl.InitialStatesMapImpl;

import java.util.Map;

public interface InitialStatesMap extends
		Map<Class<?>, Class<? extends InitialState>> {

	static InitialStatesMap create() {
		return new InitialStatesMapImpl();
	}

	@Override
	String toString();

}
