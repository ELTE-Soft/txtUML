package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelElement;

@FunctionalInterface
public interface ParameterizedBlockBody<T> extends ModelElement {

	void run(T param);

}
