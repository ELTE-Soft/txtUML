package hu.elte.txtuml.api.model.execution.impl.assoc;

import java.util.HashMap;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ModelClass;

@SuppressWarnings("serial")
public class AssociationsMap extends HashMap<Class<? extends AssociationEnd<?>>, AssociationEndRuntime<?, ?>> {

	public static AssociationsMap create() {
		return new AssociationsMap();
	}

	@SuppressWarnings("unchecked")
	public <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndRuntime<T, C> getEnd(
			Class<? extends AssociationEnd<C>> otherEnd) {
		return (AssociationEndRuntime<T, C>) super.get(otherEnd);
	}

	public <T extends ModelClass, C extends GeneralCollection<T>> void putEnd(
			Class<? extends AssociationEnd<C>> otherEnd, AssociationEndRuntime<T, C> wrapper) {
		super.put(otherEnd, wrapper);
	}

}
