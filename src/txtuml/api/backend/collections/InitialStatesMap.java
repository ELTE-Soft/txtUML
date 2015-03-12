package txtuml.api.backend.collections;

import java.util.Map;

import txtuml.api.StateMachine.InitialState;
import txtuml.api.backend.collections.impl.InitialStatesMapImpl;

public interface InitialStatesMap extends
		Map<Class<?>, Class<? extends InitialState>> {

	static InitialStatesMap create() {
		return new InitialStatesMapImpl();
	}

	@Override
	String toString();

}
