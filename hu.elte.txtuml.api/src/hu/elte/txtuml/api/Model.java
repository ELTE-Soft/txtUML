package hu.elte.txtuml.api;

/**
 * Base class of txtUML models. Read this documentation page further for an
 * overview on modeling in txtUML.
 * 
 * <p>
 * <b>Represents:</b> model
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Every element of the model must be nested classes of the same subclass of
 * <code>Model</code>.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>none</li>
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
 * <li><i>Nested classes:</i> allowed, both static and non-static, to represent
 * elements of the model; all nested classes must be subclasses of an API class
 * representing a certain model element</li>
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
 * public class SampleModel extends Model {
 * 
 * 	class SampleClass1 extends ModelClass {
 *  		//...
 * 	}
 * 
 * 	class SampleClass2 extends ModelClass {
 *  		//...
 * 	}
 *  
 * 	public static class SampleSignal extends Signal {}
 *  
 * 	//...
 * 
 * }
 * </code>
 * </pre>
 * 
 * <h1>Modeling in txtUML</h1>
 * 
 * <h2>About Java restrictions</h2>
 * 
 * <p>
 * A txtUML model is always a valid Java source but it must apply to other
 * restrictions and requirements as well. All these special rules are detailed
 * at the corresponding pages of this documentation. Read it carefully as
 * currently no model validator is implemented for txtUML so the documentation
 * is the only source of information about model validity. Offending these rules
 * may cause unexpected behavior or errors without proper explanation or error
 * messages.
 * <p>
 * <b>Note:</b> many of these rules of writing txtUML models are only in the
 * service of model exportation to EMF-UML2. Therefore, it is possible for a
 * non-valid model to be executed correctly through the API, but produce errors
 * when exporting that model to EMF-UML2.
 *
 * <h2>Main structure of a model</h2>
 * 
 * <p>
 * A txtUML model consists of a single class <i>m</i> which has to be the
 * subclass of <code>Model</code>. All parts of the model must be implemented as
 * nested classes of <i>m</i>. As a general rule, the model <b>may not</b> refer
 * to or use <i>any</i> code in <i>any</i> way that is from outside <i>m</i>,
 * with a few exceptions, described below, in the 'Global Java restrictions'
 * section.
 * 
 * <h2>Supported features</h2>
 *
 * <h3>Model features</h3>
 *
 * <ul>
 * <li>classes ( {@link ModelClass} ),</li>
 * <li>associations ( {@link Association} ),</li>
 * <li>navigable and non-navigable association ends with different
 * multiplicities ( {@link AssociationEnd} ),</li>
 * <li>collections of model objects ( {@link Collection} ),</li>
 * <li>state machines ( {@link StateMachine} ) having
 * <ul>
 * <li>initial pseudostates ( {@link StateMachine.Initial} ),</li>
 * <li>states ( {@link StateMachine.State} ) with entry (
 * {@link StateMachine.State#entry() entry} ) and exit (
 * {@link StateMachine.State#exit() exit} ) actions,</li>
 * <li>transitions ( {@link StateMachine.Transition} ) with effects (
 * {@link StateMachine.Transition#effect() effect} ) and guards (
 * {@link StateMachine.Transition#guard() guard} ),</li>
 * <li>composite states ( {@link StateMachine.CompositeState} ) to implement
 * hierarchical state machines,</li>
 * <li>choice pseudostates ( {@link StateMachine.Choice} ),</li>
 * </ul>
 * </li>
 * <li>sending simple and parametric signals ( {@link Signal},
 * {@link Action#send(ModelClass, Signal) Action.send} ),</li>
 * <li>linking, unlinking, querying associations (
 * {@link Action#link(Class, ModelClass, Class, ModelClass) Action.link},
 * {@link Action#unlink(Class, ModelClass, Class, ModelClass) Action.unlink},
 * {@link ModelClass#assoc(Class) ModelClass.assoc} ),</li>
 * <li>model object deletion ( {@link Action#delete(ModelClass) Action.delete}
 * ),</li>
 * <li>external classes to call out to native Java code ( {@link ExternalClass}
 * ),</li>
 * <li>boolean, integer, string primitive types ( {@link ModelInt},
 * {@link ModelBool}, {@link ModelString} )</li>
 * <li>control structures and variables to use inside them (
 * {@link Action#If(hu.elte.txtuml.api.blocks.Condition, hu.elte.txtuml.api.blocks.BlockBody, hu.elte.txtuml.api.blocks.BlockBody)
 * Action.If},
 * {@link Action#While(hu.elte.txtuml.api.blocks.Condition, hu.elte.txtuml.api.blocks.BlockBody)
 * Action.While},
 * {@link Action#For(ModelInt, ModelInt, hu.elte.txtuml.api.blocks.ParameterizedBlockBody)
 * Action.For}, {@link VariableType} )</li>
 * <li>logging ( {@link Action#log(String) log}, {@link Action#logError(String)
 * logError} ),</li>
 * <li>changeable timers through the standard library (
 * {@link hu.elte.txtuml.stdlib.Timer Timer},
 * {@link hu.elte.txtuml.stdlib.Timer.Handle Handle} ).
 * </ul>
 *
 * <h3>Executor features</h3>
 * 
 * <ul>
 * <li>initializing the model and sending signals to it from external code,</li>
 * <li>execution time, which makes it possible to speed up or slow down the
 * model execution (
 * {@link ModelExecutor.Settings#setExecutionTimeMultiplier(float)
 * setExecutionTimeMultiplier} ),</li>
 * <li>dynamic checks and error logging, like checking multiplicities or
 * overlapping guards, some of which might be switched off due to their high
 * cost at runtime ( {@link ModelExecutor.Settings#setDynamicChecks(boolean)
 * setDynamicChecks} ),</li>
 * <li>optional automatic logging (
 * {@link ModelExecutor.Settings#setExecutorLog(boolean) setExecutorLog} ),</li>
 * <li>setting custom <code>PrintStream</code>s to be used when logging (
 * {@link ModelExecutor.Settings#setUserOutStream(java.io.PrintStream)
 * setUserOutStream},
 * {@link ModelExecutor.Settings#setUserErrorStream(java.io.PrintStream)
 * setUserErrorStream},
 * {@link ModelExecutor.Settings#setExecutorOutStream(java.io.PrintStream)
 * setExecutorOutStream},
 * {@link ModelExecutor.Settings#setExecutorErrorStream(java.io.PrintStream)
 * setExecutorErrorStream} ).</li>
 * </ul>
 * 
 * <h2>Global Java restrictions</h2>
 * 
 * <h3>Allowed in the model</h3>
 * 
 * <ul>
 * <li>Using and extending classes, interfaces, enums and annotations of this
 * package and the {@link hu.elte.txtuml.api.blocks} package, if the opposite is
 * not stated on the corresponding pages of this documentation.</li>
 * <li>Using Java primitive types and <code>String</code>s but only as
 * parameters of constructors and setter methods of {@link ModelType} and
 * {@link VariableType} classes and their subclasses, and logging methods of
 * {@link Action} ( {@link Action#log(String) log},
 * {@link Action#logError(String) logError} ).</li>
 * <li>Using subclasses of <code>ExternalClass</code>, like the classes of the
 * {@link hu.elte.txtuml.stdlib} package. See the documentation of
 * {@link ExternalClass} for details.</li>
 * </ul>
 * 
 * <h3>Disallowed in the model</h3>
 * 
 * <ul>
 * <li>Using or extending any type not included in the preceding list, including
 * Java built-in types (with the exception of <code>java.lang.Object</code>).</li>
 * <li>Any kind of control structures (<code>if</code>, <code>while</code>,
 * <code>do</code> <code>for</code>, <code>synchronized</code>,
 * <code>switch</code> statements).</li>
 * <li>Exception handling.</li>
 * <li>Defining <code>enum</code>s, <code>interface</code>s or
 * <code>abstract class</code>es.</li>
 * <li>Defining <code>synchronized</code>, <code>native</code> or
 * <code>abstract</code> methods.</li>
 * <li>Defining local classes.</li>
 * <li>Using the <code>instanceof</code> or the ternary <code>? :</code>
 * operator.</li>
 * <li>Equality checks with operators <code>==</code> or <code>!=</code>,
 * including null-checking (<code>null</code> values should be avoided in the
 * model).</li>
 * <li>Overriding or explicitly using the methods of
 * <code>java.lang.Object</code>, with the exception of the use of the
 * <code>toString</code> method which might be used only for logging.</li>
 * <li>Creating or managing threads.</li>
 * </ul>
 * 
 * <h2>Communicating with the 'outside world'</h2>
 *
 * <p>
 * A txtUML model may never run by itself. It has to be managed or at least
 * started from the outside. As {@link ModelExecutor} (the class which is
 * responsible for executing models) uses its own thread, there are two main
 * cases of accessing the model from the outside:
 * <ul>
 * <li>The model is accessed from another thread (that is, not the executor's
 * thread).</li>
 * <li>The model calls a method of a class that is not part of the model (only
 * allowed if that class extends {@link ExternalClass}), and then the model is
 * accessed from that method (on the executor's thread).</li>
 * </ul>
 * 
 * <p>
 * For details about the second case, see the documentation of
 * {@link ExternalClass}. In the first case, when the model is accessed from
 * another thread, <b>only the following actions might be performed</b>:
 * 
 * <ul>
 * <li>Create model objects (instances of subclasses of {@link ModelClass}) by
 * either calling their constructors or using the
 * {@link Action#create(Class, ModelValue...) Action.create} method.
 * Instantiating the model class <i>m</i> (the subclass of <code>Model</code>
 * which encloses the whole model) is also allowed but is not required, neither
 * has any effect on a well-defined model.</li>
 * <li>Use the static methods of the <code>Action</code> class to
 * {@link Action#link(Class, ModelClass, Class, ModelClass) link},
 * {@link Action#unlink(Class, ModelClass, Class, ModelClass) unlink},
 * {@link Action#start(ModelClass) start} or {@link Action#delete(ModelClass)
 * delete} model objects that <b>have not been started yet</b> (the
 * <code>start</code> method was not yet called with the corresponding model
 * object as its parameter).</li>
 * <li>Change fields or call methods of model objects that <b>have not been
 * started yet</b>. Calling methods is only allowed if the method itself also
 * follows these rules. <br>
 * <b>Note:</b> the {@link ModelClass#assoc(Class) ModelClass.assoc} method
 * applies to this rule.</li>
 * <li>Create instances of signal types (subclasses of {@link Signal}) and set
 * their fields <b>before they are sent</b> to the model (with the
 * {@link Action#send(ModelClass, Signal) Action.send} method).</li>
 * <li>Use the {@link Action#send(ModelClass, Signal) send} method to send
 * signals to a model object that <b>has already been started</b>.</li>
 * <li>Call {@link Action#log(String) Action.log} or
 * {@link Action#logError(String) Action.logError} at any time.</li>
 * <li>Call {@link ModelClass#getIdentifier() ModelClass.getIdentifier} to get
 * the unique identifier of a model object at any time.</li>
 * </ul>
 * 
 * <h2>Definitions</h2>
 * 
 * <p>
 * This section is for some important definitions about txtUML which are
 * referenced throughout this documentation.
 * 
 * <h3>Action code</h3>
 * 
 * <p>
 * Java code blocks that contain txtUML action code may contain any Java code
 * that follows the rules set in the 'Global Java restrictions' section.
 * <p>
 * The most important features of the txtUML action language (the following list
 * is <i>not</i> restrictive):
 * <ul>
 * <li>Using the {@link Action} class and its static methods to
 * <ul>
 * <li>create, link, unlink, delete model objects,</li>
 * <li>send signals asynchronously to model objects,</li>
 * <li>implement control structures.</li>
 * </ul>
 * </li>
 * <li>Getting/setting fields of model objects (as UML attributes), calling
 * their methods (as UML operations).</li>
 * <li>Querying association ends with the {@link ModelClass#assoc(Class)
 * ModelClass.assoc} method, using the result through the {@link Collection}
 * interface.</li>
 * <li>Using subclasses of {@link ExternalClass} to bring external features into
 * the model or to communicate with components of the program that are not part
 * of the model.</li>
 * </ul>
 * 
 * <h3>Condition evaluation</h3>
 * 
 * <p>
 * A condition evaluation in txtUML is a Java code block which aims to create a
 * {@link ModelBool} value that represents either <code>true</code> or
 * <code>false</code> whether a certain condition about the model (which
 * condition the block represents) holds or not.
 * <p>
 * Condition evaluations in txtUML include
 * {@link StateMachine.Transition#guard() guards} of transitions, conditions of
 * {@link Action#If(hu.elte.txtuml.api.blocks.Condition, hu.elte.txtuml.api.blocks.BlockBody, hu.elte.txtuml.api.blocks.BlockBody)
 * If} statements and certain <code>Collection</code> methods (
 * {@link Collection#selectAll(hu.elte.txtuml.api.blocks.ParameterizedCondition)
 * selectAll} ).
 * <p>
 * A condition evaluation is exported to EMF-UML2 as a single model query, so it
 * <b>may include only the following actions</b>:
 * <ul>
 * <li>Defining, modifying local variables.</li>
 * <li>Getting (but <i>not</i> setting) fields of model objects and signals.</li>
 * <li>Querying association ends with the {@link ModelClass#assoc(Class)
 * ModelClass.assoc} method.</li>
 * <li>Creating new {@link ModelType} or {@link Collection} instances with
 * methods of those two types or their subtypes. (As all such instances are
 * immutable, this does not affect the state of the model in any way.)</li>
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
 * @author Gabor Ferenc Kovacs
 * @see ModelClass
 * @see Association
 * @see Signal
 * 
 */
public class Model extends Action {

	/**
	 * Sole constructor of <code>Model</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Protected because this class is intended to be inherited from but not
	 * instantiated. However, <code>Model</code> has to be a non-abstract class
	 * to make sure that it is instantiatable when that is needed for the API or
	 * the model exportation.
	 */
	protected Model() {
	}

}