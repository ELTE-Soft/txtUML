package txtuml.api.blocks;

import txtuml.api.ModelElement;

@FunctionalInterface
public interface ParameterizedBlockBody<T> extends ModelElement {

	void run(T param);

}
