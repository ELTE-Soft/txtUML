package hu.elte.txtuml.api.model;

// TODO document
public abstract class AssociationEnd<T> {

	AssociationEnd() {
	}

	// CONTAINMENT_KIND AND NAVIGABILITY
	
	/**
	 * Implementing classes represent container association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 * 
	 * @see hu.elte.txtuml.api.model.Composition
	 */
	public interface ContainerEnd extends ContainmentKind<ContainerEnd> {
	}

	/**
	 * Implementing classes represent <i>non</i>-container association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface NonContainerEnd extends ContainmentKind<NonContainerEnd> {
	}

	/**
	 * Implementing classes of this interface represent navigable association
	 * ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface Navigable extends Navigability<Navigable> {
	}

	/**
	 * Implementing classes of this interface represent non-navigable
	 * association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface NonNavigable extends Navigability<NonNavigable> {
	}

	// PRIVATE INTERFACES

	/**
	 * A base interface to represent the navigability property of association
	 * ends. Association ends might be {@link Navigable} or {@link NonNavigable}
	 * .
	 */
	private interface Navigability<T extends Navigability<T>> {
	}

	/**
	 * A base interface to represent whether a certain association end is a
	 * container end or not. An association end is either a {@link ContainerEnd}
	 * or a {@link NonContainerEnd}.
	 */
	private interface ContainmentKind<T extends ContainmentKind<T>> {
	}

}
