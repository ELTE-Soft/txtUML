package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.ContainmentKind;
import hu.elte.txtuml.api.model.assocends.Multiplicity;
import hu.elte.txtuml.api.model.assocends.Navigability;

/**
 * A base class for a composition associations in the model.
 * 
 * <p>
 * <b>Represents:</b> composition (a special case of association)
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * A composition in the model is a subclass of <code>Composition</code>, having
 * two inner classes which both extend {@link AssociationEnd}. One of these ends
 * must be the container of the members at the other end. The container must be
 * a subclass of <code>Container</code> or <code>HiddenContainer</code>. The
 * other end can be a subclass of any non-composite <code>AssociationEnd</code>.
 * <p>
 * The two model classes which the association connects are defined by the two
 * association ends' generic parameters.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * <li>must have two inner classes which are subclasses of
 * <code>AssociationEnd</code>, exactly one being a subclass of
 * <code>Container</code></li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> allowed at most two, both of which are non-static
 * and are subclasses of <code>AssociationEnd</code>. Exactly one of them must
 * be a subclass of <code>Container</code>.</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class SampleAssociation extends Composition {
 * 	class ContainerEnd extends {@literal Container<SampleClass2>} {}
 * 	class PartEnd extends {@literal Many<SampleClass1>} {}
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @see Association
 * @see Container
 * @see HiddenContainer
 */
public abstract class Composition extends Association {

	/**
	 * Abstract base class for the navigable container end of a composition
	 * association which is a special association end with 0..1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable container end of a composition association
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * The container end should be defined as inner class of a composition (a
	 * subclass of {@link Composition}).
	 * <p>
	 * Container ends have an implicit 0..1 multiplicity. They also have a
	 * <b>global restriction</b>: any model object at any time might be
	 * connected through its compositions to at most one container object.
	 * <p>
	 * Apart from this, container ends should be used like any other association
	 * end. See the documentation of {@link AssociationEnd} for details.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Association} for details on defining and
	 * using associations and {@link Composition} for associations structurally
	 * containing the associated values.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class Container<T extends ModelClass> extends MaybeEnd<T>
			implements Multiplicity.ZeroToOne, Navigability.Navigable, ContainmentKind.ContainerEnd {
	}

	/**
	 * Abstract base class for the non-navigable container end of a composition
	 * association which is a special association end with 0..1 multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable container end of a composition
	 * association
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link Container}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Association} for details on defining and
	 * using associations and {@link Composition} for associations structurally
	 * containing the associated values.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenContainer<T extends ModelClass> extends MaybeEnd<T>
			implements Multiplicity.ZeroToOne, Navigability.NonNavigable, ContainmentKind.ContainerEnd {
	}

}
