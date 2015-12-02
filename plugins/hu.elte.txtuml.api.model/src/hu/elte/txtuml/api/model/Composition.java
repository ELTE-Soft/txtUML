package hu.elte.txtuml.api.model;

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
 * a subclass of <code>Container</code>. The other end can be a subclass of any
 * <code>AssociationEnd</code>
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
 * <li>must be the nested class of a subclass of {@link Model}</li>
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
 * @author Gabor Ferenc Kovacs
 * @see Association
 */
public class Composition extends Association {

	protected Composition() {

	}

}
