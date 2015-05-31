package hu.elte.txtuml.api.backend.collections;

import hu.elte.txtuml.api.Region;
import hu.elte.txtuml.api.StateMachine.Initial;
import hu.elte.txtuml.api.backend.collections.impl.InitialsMapImpl;

import java.util.Map;

/**
 * A mapping of classes to subclasses of {@link Initial}. Intended for the
 * {@link Region} class where it is used to cache the classes representing the
 * initial pseudostates of regions and composite states.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface InitialsMap extends Map<Class<?>, Class<? extends Initial>> {

	/**
	 * Creates a new <code>InitialsMap</code> instance.
	 * 
	 * @return the new instance
	 */
	static InitialsMap create() {
		return new InitialsMapImpl();
	}

	@Override
	String toString();

}
