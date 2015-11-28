package hu.elte.txtuml.api.model.blocks;

import hu.elte.txtuml.api.model.ModelElement;

/**
 * A functional interface to implement code blocks in the action language.
 * 
 * <p>
 * <b>Represents:</b> code block
 * <p>
 * 
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview on
 * modeling in JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
@FunctionalInterface
public interface BlockBody extends ModelElement {

	/**
	 * The sole method of <code>BlockBody</code>. Override this method to
	 * implement the desired code block.
	 * <p>
	 * Overriding methods may only contain action code. See the documentation of
	 * {@link hu.elte.txtuml.api.model.Model} for details about the action language.
	 */
	void run();

}
