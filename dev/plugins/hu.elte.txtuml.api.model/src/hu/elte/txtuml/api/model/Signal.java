package hu.elte.txtuml.api.model;

/**
 * Base class for signals in the model.
 * 
 * <p>
 * <b>Represents:</b> signal, signal event
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Inherit signals from this class. To keep modeling simple, a signal is also a
 * signal event in JtxtUML so it can be used as a trigger of transitions
 * directly. As a signal event, it is considered to contain itself as a signal.
 * <p>
 * Signals might be inherited from other signals as well. This is represented in
 * the model by defining a subclass of another signal class. It means that
 * because the limitations of the Java language, every signal may have at most
 * one parent signal.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li><i>none</i></li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Constructors:</i> allowed, containing only simple assignments to set
 * the values of its fields</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> allowed, only of primitive types (including
 * String), model enums or data types to represent parameters of the signal</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed to represent signal
 * inheritance</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class SampleSignal extends Signal {}
 * 
 * {@literal //} somewhere in the action code:
 * 
 * Action.send(obj, new SampleSignal());
 * </code>
 * </pre>
 *
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class Signal extends Event {

	@Override
	public String toString() {
		return "signal:" + getClass().getSimpleName();
	}

}