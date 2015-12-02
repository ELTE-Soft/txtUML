package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.Navigability.Navigable;

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
	
	/**
	 * Abstract base class for the container end of a composition association.
	 * 
	 * <p>
	 * <b>Represents:</b> container end of a composition association
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * The container end should be defined as inner class of a composition (a
	 * subclass of {@link Composition}).
	 * <p>
	 * When asked for the model objects at an association end (with the
	 * {@link ModelClass#assoc(Class) assoc} method), an instance of the actual
	 * representing class of that certain association end will be returned. As
	 * all association ends are implementing the {@link Collection} interface,
	 * the contained objects might be accessed through the collection methods.
	 * <p>
	 * The multiplicity of an association end might only be checked during model
	 * execution. The upper bound is always checked, if it is ever offended, an
	 * error message is shown in the model executor's error log (see the
	 * documentation of the
	 * {@link ModelExecutor.Settings#setExecutorErrorStream(java.io.PrintStream)
	 * ModelExecutor.Settings.setExecutorErrorStream} method). However, this
	 * does not cause the execution to fail. The lower bound might be offended
	 * temporarily, but has to be restored before the current <i>execution
	 * step</i> ends. It is checked at the beginning of the next <i>execution
	 * step</i> and an error message is shown if it is still offended and the
	 * regarding model object is not in {@link ModelClass.Status#DELETED
	 * DELETED} status. However, as this check is relatively slow, it might be
	 * switched off along with other optional checks with the
	 * {@link ModelExecutor.Settings#setDynamicChecks(boolean)
	 * ModelExecutor.Settings.setDynamicChecks} method.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed, inherit from its predefined
	 * subclasses instead (see the inner classes of {@link Association})</li>
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
	 * @author Gabor Ferenc Kovacs
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class Container<T extends ModelClass> extends OneBase<T>
			implements hu.elte.txtuml.api.model.assocends.Multiplicity.One, Navigable {
	}

}
