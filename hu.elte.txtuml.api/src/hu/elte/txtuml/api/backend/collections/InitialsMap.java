package hu.elte.txtuml.api.backend.collections;

import hu.elte.txtuml.api.StateMachine.Initial;
import hu.elte.txtuml.api.backend.collections.impl.InitialsMapImpl;

import java.util.Map;

public interface InitialsMap extends
		Map<Class<?>, Class<? extends Initial>> {

	static InitialsMap create() {
		return new InitialsMapImpl();
	}

	@Override
	String toString();

}
