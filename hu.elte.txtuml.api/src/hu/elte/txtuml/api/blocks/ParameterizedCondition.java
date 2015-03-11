package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelElement;

@FunctionalInterface
public interface ParameterizedCondition<T> extends ModelElement {

	ModelBool check(T selected);

}