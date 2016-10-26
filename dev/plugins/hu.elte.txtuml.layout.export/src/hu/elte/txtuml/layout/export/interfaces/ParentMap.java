package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;

import hu.elte.txtuml.layout.export.impl.ParentMapImpl;

public interface ParentMap extends Map<Class<?>, Class<?>> {

	
	static ParentMap create() {
		return new ParentMapImpl();
	}
	
	boolean isInParent();
	
	Class<?> put(Class<?> subClass);
	
	void addNew(Class<?> parent);
	
	Class<?> getLast();
	
	Class<?> removeLast();
	
}
