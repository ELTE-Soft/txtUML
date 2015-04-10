package hu.elte.txtuml.api;

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
 * signal event in txtUML so it can be used as a trigger of transitions
 * directly. As a signal event, it is considered to contain itself as a signal.
 * <p>
 * Signals might be inherited from other signals as well. This is represented in
 * the model by defining a subclass of another signal class. It means, that
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
 * <li>must be the nested class of a subclass of {@link Model}</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Constructors:</i> allowed, containing only simple assignments to set
 * the values of its fields</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> allowed to represent parameters of the signal</li>
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
 * <p>
 * 
 * <pre>
 * <code>
 * class ExampleSignal extends Signal {}
 * 
 * {@literal /}{@literal /} ... somewhere in the action code
 * 
 * Action.send(obj, new ExampleSignal());
 * </code>
 * </pre>
 *
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
@ModelAnnotatedElement
public class Signal extends Event {

	/**
	 * Sole constructor of <code>Signal</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Protected because this class is intended to be inherited from but not
	 * instantiated. However, <code>Signal</code> has to be a non-abstract class
	 * to make sure that it is instantiatable when that is needed for the API or
	 * the model exportation.
	 */
	protected Signal() {
	}

	@Override
	public String toString() {
		return "signal_" + super.toString();
	}

}