package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.primitives.ModelBool;

@FunctionalInterface
public interface ParameterizedCondition<T> extends ModelElement {

	ModelBool check(T selected);

}