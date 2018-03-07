package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation shows that the annotated package and its subpackages form a
 * JtxtUML model. Read this documentation page further for an overview on
 * modeling in JtxtUML.
 * 
 * <p>
 * <b>Represents:</b> model
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Apply on the top package of a JtxtUML model. Annotations on packages should
 * be defined in {@code package-info.java} file.
 * <p>
 * Set {@link #value() value} to define a unique name of the model.
 * <p>
 * If package <i>A</i> is marked as a model and <i>B</i> is a subpackage of
 * <i>A</i> then <i>B</i> cannot be marked to be a model as it is already a part
 * of one.
 * <p>
 * If a package is marked as a model then all {@code .java} files in that
 * package and its subpackages are considered parts of the model and therefore
 * should be valid model elements.
 * </p>
 * 
 * <h1>Modeling in JtxtUML</h1>
 * 
 * <h2>About Java restrictions</h2>
 * 
 * <p>
 * A JtxtUML model is always a valid Java source but it must apply to other
 * restrictions and requirements as well. All these special rules are detailed
 * at the corresponding pages of this documentation.
 *
 * <h2>Main structure of a model</h2>
 * 
 * <p>
 * A JtxtUML model consists of a package structure under one particular package
 * that has to be annotated with {@link Model}. All parts of the model must be
 * implemented as contents of this package or its subpackages. As a general
 * rule, the model <b>may not</b> refer to or use <i>any</i> code in <i>any</i>
 * way that is from outside this package structure, with a few exceptions,
 * described below, in the 'Global Java restrictions' section.
 * 
 * <h2>Global Java restrictions</h2>
 * 
 * <h3>Allowed in the model</h3>
 * 
 * <ul>
 * <li>Using and extending classes, interfaces, enums and annotations of this
 * package (hu.elte.txtuml.api.model), if the opposite is not stated on the
 * corresponding pages of this documentation.</li>
 * <li>Using and extending types of other txtUML models.</li>
 * <li>Using Java primitive types and <code>String</code>s.</li>
 * </ul>
 * 
 * <h3>Disallowed in the model</h3>
 * 
 * <ul>
 * <li>Using or extending any type not included in the preceding list, including
 * Java built-in types (with the exception of <code>java.lang.Object</code>).
 * </li>
 * <li>Exception handling.</li>
 * <li>Defining <code>abstract class</code>es.</li>
 * <li>Defining <code>synchronized</code>, <code>native</code> or
 * <code>abstract</code> methods, using <code>synchronized</code> blocks.</li>
 * <li>Defining local classes.</li>
 * <li>Creating or managing threads.</li>
 * </ul>
 * 
 * <h2>Communicating with the 'outside world'</h2>
 *
 * <p>
 * A JtxtUML model may never run by itself. It has to be managed or at least
 * started from the outside. As model executors use their own threads, there are
 * two main cases of accessing the model from the outside:
 * <ul>
 * <li>The model is accessed from another thread (that is, not the executor's
 * thread).</li>
 * <li>The model calls a method of a class the body of which is not part of the
 * model (only allowed if that method is marked with {@link ExternalBody}).</li>
 * </ul>
 * 
 * <p>
 * In the first case, when the model is accessed <b>from another thread, only a
 * small set of safe operations</b> can be done which are listed in the
 * {@link API} class. This class cannot be used from the model but its static
 * methods are the only ones which can be called from any other thread.
 * 
 * <h2>Definitions</h2>
 * 
 * <p>
 * This section is for some important definitions about JtxtUML which are
 * referenced throughout this documentation.
 * 
 * <h3>Action code</h3>
 * 
 * <p>
 * Java code blocks that contain JtxtUML action code may contain any Java code
 * that follows the rules set in the 'Global Java restrictions' section.
 * <p>
 * The most important features of the JtxtUML action language (the following
 * list is <i>not</i> restrictive):
 * <ul>
 * <li>Using the {@link Action} class and its static methods.</li>
 * <li>Getting/setting fields of model objects (as UML attributes), calling
 * their methods (as UML operations).</li>
 * <li>Querying association ends with the {@link ModelClass#assoc(Class)
 * ModelClass.assoc} method, using the result through the returned collection
 * type.</li>
 * </ul>
 * 
 * <h3>Condition evaluation</h3>
 * 
 * <p>
 * A condition evaluation in JtxtUML is a Java code block which aims to create a
 * <code>boolean</code> value that represents whether a certain condition about
 * the model (which condition the block represents) holds or not.
 * <p>
 * Condition evaluations in JtxtUML include
 * {@link StateMachine.Transition#guard() guards} of transitions.
 * <p>
 * A condition evaluation is exported as a single model query, so it <b>may
 * include only the following actions</b>:
 * <ul>
 * <li>Defining, modifying local variables.</li>
 * <li>Getting (but <i>not</i> setting) fields of model objects and signals.
 * </li>
 * <li>Querying association ends with the {@link ModelClass#assoc(Class)
 * ModelClass.assoc} method.</li>
 * <li>Using primitives (including <code>Sting</code>).</li>
 * <li>Creating {@link GeneralCollection} instances with the
 * {@link Action#collect}, {@link Action#collectIn} methods or methods of the
 * collection types. (As all such instances are immutable, this does not affect
 * the state of the model in any way.)</li>
 * <li>Calling methods of model objects as operations.</li>
 * </ul>
 * 
 * <h3>Execution step</h3>
 * 
 * <p>
 * An execution step starts when an asynchronous event (like a signal event) is
 * chosen by the executor to be processed and ends when that event and all the
 * synchronous events caused by it (like a state machine changing state, entry
 * and exit actions, transition effects, operation calls) have been processed.
 *
 * @see ModelClass
 * @see Association
 * @see Signal
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
@Documented
public @interface Model {

	/**
	 * Sets the name of the model.
	 */
	String value() default "";

}