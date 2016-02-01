package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A runtime annotation to define the target vertex of a transition.
 * 
 * <p>
 * <b>Represents:</b> target property of a transition
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Use on a subclass of {@link hu.elte.txtuml.api.model.StateMachine.Transition
 * Transition} and set {@link #value() value} to reference the representing class
 * of the desired vertex.
 * <p>
 * If used on class <i>t</i> with the value referencing class <i>v</i>, the
 * following conditions must be met:
 * <ul>
 * <li><i>t</i> is a subclass of <code>Transition</code> and not
 * <code>Transition</code> itself,</li>
 * <li><i>v</i> is a subclass of <code>Vertex</code> and not <code>Vertex</code>
 * itself or any subclasses of <code>Vertex</code> defined as inner classes of
 * {@link StateMachine},
 * <li><i>t</i> and <i>v</i> must be inner classes of the same enclosing class,
 * either a subclass of {@link Region} or
 * {@link hu.elte.txtuml.api.model.StateMachine.CompositeState CompositeState},
 * <p>
 * <b>Note:</b> {@link ModelClass} is a subclass of <code>Region</code>.</li>
 * </ul>
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
 * See the documentation of {@link Model} for an overview on modeling in JtxtUML.
 *
 * @see StateMachine.Transition
 * @see From
 * @see Trigger
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface To {

	/**
	 * The target vertex of the transition this annotation is used on.
	 */
	Class<? extends StateMachine.Vertex> value();

}