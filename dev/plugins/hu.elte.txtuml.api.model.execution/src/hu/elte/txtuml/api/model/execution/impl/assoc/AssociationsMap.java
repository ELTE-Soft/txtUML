package hu.elte.txtuml.api.model.execution.impl.assoc;

import java.util.HashMap;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;

@SuppressWarnings("serial")
public class AssociationsMap extends HashMap<Class<? extends AssociationEnd<?, ?>>, AssociationEndWrapper<?, ?>> {

	public static AssociationsMap create() {
		return new AssociationsMap();
	}

	@SuppressWarnings("unchecked")
	public <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> getEnd(
			Class<? extends AssociationEnd<T, C>> otherEnd) {
		return (AssociationEndWrapper<T, C>) super.get(otherEnd);
	}

	public <T extends ModelClass, C extends Collection<T>> void putEnd(Class<? extends AssociationEnd<T, C>> otherEnd,
			AssociationEndWrapper<T, C> wrapper) {
		super.put(otherEnd, wrapper);
	}

}
