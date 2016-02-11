package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A runtime annotation to define the trigger of a transition.
 * 
 * <p>
 * <b>Represents:</b> trigger of a transition
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Use on a subclass of {@link hu.elte.txtuml.api.model.StateMachine.Transition
 * Transition} and set the value of the {@link #value() value} element to
 * reference the representing class of the desired signal (as a signal event).
 * The current implementation does not support any other kind of events on
 * triggers.
 * <p>
 * Only one trigger might be applied to a transition.
 * <p>
 * Transitions are only allowed to have no triggers if they are from an initial
 * pseudostate or a choice pseudostate. Therefore, transitions not included in
 * these two cases must have exactly one trigger defined.
 * <p>
 * It is also possible to restrict the trigger to only accept signals that
 * appear on a specific port. For this, set a value to the {@link Trigger#port()
 * port} element.
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * {@literal @From(SourceState.class) @To(TargetState.class) @Trigger(SampleSignal.class)}
 * class SampleTransition extends Transition {}
 * </code>
 * </pre>
 * 
 * See the documentation of {@link StateMachine} for detailed examples.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @see StateMachine.Transition
 * @see To
 * @see From
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Trigger {

	/**
	 * The triggering signal of the transition this annotation is used on.
	 */
	Class<? extends Signal> value();

	/**
	 * The behavior port on which the triggering signal must appear. By default,
	 * it equals to {@link AnyPort AnyPort.class}, which means that the
	 * represented trigger may accept signals from any port (including cases
	 * when the signal does arrive through a port).
	 */
	Class<? extends Port<?, ?>> port() default AnyPort.class;

	/**
	 * A special port type which is only used to mark at {@link Trigger#port()}
	 * that the represented trigger may accept signals from any port (or even
	 * signals that do not arrive through ports).
	 * 
	 * <p>
	 * <b>Represents:</b> any port type
	 * <p>
	 * <b> Usage: </b>
	 * <p>
	 * As {@link Trigger#port()} equals to {@code AnyPort.class} by default,
	 * this type does not need to be used in JtxtUML models directly.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed</li>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples about
	 * defining state machines.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 */
	abstract class AnyPort extends Port<Interface, Interface> {

		private AnyPort() {
		}

	}

}
