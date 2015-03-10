package hu.elte.txtuml.api.util.collections;

import hu.elte.txtuml.api.Association.AssociationEnd;
import hu.elte.txtuml.api.util.collections.impl.AssociationsMapImpl;

import java.util.Map;

public interface AssociationsMap extends
		Map<Class<? extends AssociationEnd<?>>, AssociationEnd<?>> {

	static AssociationsMap create() {
		return new AssociationsMapImpl();
	}

	@Override
	public String toString();

}
