package txtuml.api.blocks;

import txtuml.api.ModelElement;

@FunctionalInterface
public interface BlockBody extends ModelElement {

	void run();

}
