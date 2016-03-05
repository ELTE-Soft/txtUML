package hu.elte.txtuml.api.model.assocends;

/**
 * A base interface to represent whether a certain association end is a
 * container end or not.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 * 
 * @see Composition
 */
public interface ContainmentKind<T extends ContainmentKind<T>> {

	/**
	 * Implementing classes represent <i>non</i>-container association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 * 
	 * @see Composition
	 */
	interface SimpleEnd extends ContainmentKind<SimpleEnd> {
	}

	/**
	 * Implementing classes represent container association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 * 
	 * @see Composition
	 */
	interface ContainerEnd extends ContainmentKind<ContainerEnd> {
	}

}
