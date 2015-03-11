package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelElement;

@FunctionalInterface
public interface Condition extends ModelElement {
	
	ModelBool check();
	
}
