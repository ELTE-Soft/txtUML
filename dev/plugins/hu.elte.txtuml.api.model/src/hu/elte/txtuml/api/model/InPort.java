package hu.elte.txtuml.api.model;

/**
 * A base class for ports with an empty provided interface. It is only a
 * syntactic sugar in JtxtUML, the following two definitions are semantically
 * equal:
 * 
 * <pre>
 * <code>
 * class SampleClass extends ModelClass {
 * 
 * 	class SamplePort extends InPort{@literal <SampleInterface>} {}
 * 
 * 	// ...
 * }
 * </code>
 * </pre>
 * and
 * <pre>
 * <code>
 * class SampleClass extends ModelClass {
 * 
 * 	class SamplePort extends Port{@literal <SampleInterface, Interface.Empty>} {}
 * 
 * 	// ...
 * }
 * </code>
 * </pre>
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be the inner class of a model class (a subclass of
 * {@link ModelClass})</li>
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
 * @param <R>
 *            the required interface
 */
public abstract class InPort<R extends Interface> extends Port<R, Interface.Empty> {

	protected InPort() {
		super(new Interface.Empty() {});
	}
	
}
