package hu.elte.txtuml.api.model.blocks;

import hu.elte.txtuml.api.model.ModelElement;

/**
 * A functional interface to implement parameterized code blocks in the action
 * language.
 * 
 * <p>
 * <b>Represents:</b> code block
 * <p>
 * 
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview on
 * modeling in JtxtUML.
 * 
 * @param <T>
 *            the type of the parameter of the block
 */
@FunctionalInterface
public interface ParameterizedBlockBody<T> extends ModelElement {

	/**
	 * The sole method of <code>ParameterizedBlockBody</code>. Override this
	 * method to implement the desired code block.
	 * <p>
	 * Overriding methods may only contain action code. See the documentation of
	 * {@link hu.elte.txtuml.api.model.Model} for details about the action language.
	 * 
	 * @param param
	 *            the parameter of the block
	 */
	void run(T param);

}
