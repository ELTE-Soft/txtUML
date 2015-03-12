package txtuml.api.blocks;

import txtuml.api.ModelBool;
import txtuml.api.ModelElement;

@FunctionalInterface
public interface Condition extends ModelElement {

	ModelBool check();

}
