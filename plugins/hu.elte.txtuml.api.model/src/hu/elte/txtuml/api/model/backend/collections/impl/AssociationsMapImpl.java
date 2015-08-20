package hu.elte.txtuml.api.model.backend.collections.impl;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.backend.collections.AssociationsMap;

import java.util.HashMap;

/**
 * Default implementation for {@link AssociationsMap}.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link HashMap}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 */
@SuppressWarnings("serial")
public class AssociationsMapImpl extends
		HashMap<Class<? extends AssociationEnd<?>>, AssociationEnd<?>>
		implements AssociationsMap {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{\n");
		for (Class<? extends AssociationEnd<?>> key : this.keySet()) {
			builder.append("\t");
			builder.append(key.getSimpleName());
			builder.append(": ");
			builder.append(this.get(key));
			builder.append("\n");
		}
		builder.append("}");
		return builder.toString();
	}

}
