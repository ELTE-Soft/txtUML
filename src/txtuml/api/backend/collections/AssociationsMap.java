package txtuml.api.backend.collections;

import java.util.Map;

import txtuml.api.Association.AssociationEnd;
import txtuml.api.backend.collections.impl.AssociationsMapImpl;

public interface AssociationsMap extends
		Map<Class<? extends AssociationEnd<?>>, AssociationEnd<?>> {

	static AssociationsMap create() {
		return new AssociationsMapImpl();
	}

	@Override
	public String toString();

}
