package hu.elte.txtuml.api.blocks;

import hu.elte.txtuml.api.ModelElement;

/**
 * A functional interface to implement code blocks in the action language.
 * 
 * <p>
 * <b>Represents:</b> a code block
 * <p>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
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
	 * the {@link hu.elte.txtuml.api} package for details about the action
	 * language.
	 */
	void run();

}
