package txtuml.api.backend.collections.impl;

import java.util.HashMap;

import txtuml.api.Association.AssociationEnd;
import txtuml.api.backend.collections.AssociationsMap;

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
