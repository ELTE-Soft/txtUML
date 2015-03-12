package txtuml.api.blocks;

import txtuml.api.ModelBool;
import txtuml.api.ModelElement;

@FunctionalInterface
public interface ParameterizedCondition<T> extends ModelElement {

	ModelBool check(T selected);

}