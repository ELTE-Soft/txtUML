package hu.elte.txtuml.api.model;

/**
 * Abstract base class for association ends. Nested interfaces define certain
 * properties of association ends.
 * 
 * <p>
 * <b>Represents:</b> association end
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * See the documentation of {@link Association} for details on defining and
 * using associations.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, use its subtypes
 * {@link Association.End}, {@link Association.HiddenEnd},
 * {@link Composition.ContainerEnd} or
 * {@link Composition.HiddenContainerEnd}</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @param <C>
 *            the type of collections that contain the model objects at this end
 *            of the association; this type parameter set the multiplicity,
 *            ordering and uniqueness of the association end
 */
public abstract class AssociationEnd<C> {

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
	@External
	public interface Container extends ContainmentKind<Container> {
	}

	/**
	 * Implementing classes represent <i>non</i>-container association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	@External
	public interface NonContainer extends ContainmentKind<NonContainer> {
	}

	/**
	 * Implementing classes of this interface represent navigable association
	 * ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 * 
	 * @see Association.End
	 */
	@External
	public interface Navigable extends Navigability<Navigable> {
	}

	/**
	 * Implementing classes of this interface represent non-navigable
	 * association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 * 
	 * @see Association.HiddenEnd
	 */
	@External
	public interface NonNavigable extends Navigability<NonNavigable> {
	}

	// PRIVATE INTERFACES

	/**
	 * This interface exists to make sure that no association end can be the
	 * subtype of {@link Navigable} and {@link NonNavigable} at the same time.
	 * It would be a compile time error because of the restrictions of Java on
	 * type parameters.
	 */
	private interface Navigability<T extends Navigability<T>> {
	}

	/**
	 * This interface exists to make sure that no association end can be the
	 * subtype of {@link Container} and {@link NonContainer} at the same time.
	 * It would be a compile time error because of the restrictions of Java on
	 * type parameters.
	 */
	private interface ContainmentKind<T extends ContainmentKind<T>> {
	}

}
