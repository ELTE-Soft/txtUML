package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelElement;

@FunctionalInterface
public interface BlockBody extends ModelElement {

	void run();

}
