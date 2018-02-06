package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.AssociationEnd.NonContainer;
import hu.elte.txtuml.api.model.AssociationEnd.NonNavigable;

/**
 * A base class for associations in the model.
 * 
 * <p>
 * <b>Represents:</b> association
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * An association in the model is a subclass of <code>Association</code>, having
 * two inner classes which both extend {@link AssociationEnd}. In case of simple
 * associations (not {@link Composition}s), both of these ends have to be a
 * subtype of either {@link End} or {@link HiddenEnd}, which represent navigable
 * and non-navigable association ends, respectively.
 * <p>
 * When defining an association end, the type parameter of the extended
 * association end type must be explicitly set, like in the example below. The
 * collection type specified here will tell
 * <ul>
 * <li>the type of model objects at that association end; and also</li>
 * <li>the multiplicity,</li>
 * <li>the ordering,</li>
 * <li>and the uniqueness</li>
 * </ul>
 * of the association end.
 * <p>
 * Associations can be linked and unlinked with the appropriate actions, see
 * {@link Action#link} and {@link Action#unlink} for further information. The
 * collection of objects linked to a specific object can be obtained with the
 * {@link ModelClass#assoc} method as shown in the example below.
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
 * <code>AssociationEnd</code></li>
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
 * and are subclasses of <code>AssociationEnd</code>.</li>
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
 * class A_B extends Association {
 * 	class a extends {@literal End<Any<A>>} {}
 * 	class b extends {@literal HiddenEnd<One<B>>} {}
 * }
 * </code>
 * </pre>
 * 
 * Inside an operation of {@code B}:
 * 
 * <pre>
 * <code>
 * 	{@literal Any<A> anyOfA = this.assoc(A_B.a.class);}
 * 	A a = anyOfA.one();
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @see End
 * @see HiddenEnd
 * @see Composition
 */
public abstract class Association {

	/**
	 * Abstract base class for navigable association ends.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end
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
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <C>
	 *            the type of collections that contain the model objects at this
	 *            end of the association; this type parameter set the
	 *            multiplicity, ordering and uniqueness of the association end
	 */
	public abstract class End<C extends GeneralCollection<? extends ModelClass>> extends AssociationEnd<C>
			implements NonContainer, Navigable {
	}

	/**
	 * Abstract base class for non-navigable association ends.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end
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
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <C>
	 *            the type of collections that contain the model objects at this
	 *            end of the association; this type parameter set the
	 *            multiplicity, ordering and uniqueness of the association end
	 */
	public abstract class HiddenEnd<C extends GeneralCollection<? extends ModelClass>> extends AssociationEnd<C>
			implements NonContainer, NonNavigable {
	}

}
