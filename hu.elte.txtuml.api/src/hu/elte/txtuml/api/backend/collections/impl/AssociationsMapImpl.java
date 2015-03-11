package hu.elte.txtuml.api.backend.collections.impl;

import hu.elte.txtuml.api.Association.AssociationEnd;
import hu.elte.txtuml.api.backend.collections.AssociationsMap;

import java.util.HashMap;

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
