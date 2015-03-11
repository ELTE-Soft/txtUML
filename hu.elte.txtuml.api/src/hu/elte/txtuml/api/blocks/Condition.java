package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.primitives.ModelBool;

@FunctionalInterface
public interface Condition extends ModelElement {

	ModelBool check();

}
