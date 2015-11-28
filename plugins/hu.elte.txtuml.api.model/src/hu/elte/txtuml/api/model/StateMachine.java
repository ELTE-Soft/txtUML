package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.backend.ElseException;

/**
 * Base class for state machines in the model.
 * 
 * <p>
 * <b>Represents:</b> state machine
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * <code>StateMachine</code> might not be used directly. However,
 * {@link ModelClass} itself extends <code>StateMachine</code> and therefore
 * every model class may have an implicit state machine by defining vertices and
 * transitions as its inner classes.
 * <p>
 * A JtxtUML state machine consists of inner classes of a subclass of
 * <code>StateMachine</code>, all of which inner classes extend either
 * {@link StateMachine.Vertex Vertex} or {@link StateMachine.Transition
 * Transition}. A single initial pseudostate (a subclass of
 * {@link StateMachine.Initial Initial}) and a single unlabeled transition (one
 * that has no {@link Trigger} annotation defined) from that initial pseudostate
 * is required, without it, the state machine is ill-formed and does not run.
 * Therefore, a <code>ModelClass</code> might either have a complete state
 * machine by applying to these rules or no state machine at all (no inner
 * classes extending <code>Vertex</code> or <code>Transition</code>).
 * <p>
 * Apart from the obligatory initial pseudostate and initial transition, a
 * JtxtUML state machine may contain any number of states (subclasses of
 * {@link State}), choice pseudostates (subclasses of {@link Choice}), composite
 * states (subclasses of {@link CompositeState}) to implement hierarchical state
 * machines, and transitions (subclasses of {@link Transition}). For details
 * about their use, see the documentation of the regarding classes. For
 * examples, see below.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, extend {@link ModelClass}</li>
 * </ul>
 * 
 * <p>
 * <b>Examples:</b>
 * <p>
 * 
 * The minimal state machine consists of an initial pseudostate, a state and a
 * transition between the two.
 * 
 * <pre>
 * <code>
 * class SampleClass extends ModelClass {
 * 
 * 	class Init extends Initial {}
 * 
 * 	class S1 extends State {}
 * 
 * 	{@literal @From(Init.class) @To(S1.class)}
 * 	class Initialize extends Transition {}
 * }
 * </code>
 * </pre>
 * 
 * Guards and effects might be applied to transitions. For the example, let us
 * assume that the signal <code>SampleSignal</code> has a field (a signal
 * parameter) <code>sampleParam</code> of type int.
 * 
 * <pre>
 * <code>
 * class SampleClass2 extends ModelClass {
 * 
 * 	//...
 * 
 * 	{@literal @From(SourceState.class) @To(TargetState.class) @Trigger(SampleSignal.class)}
 * 	class SampleTransition extends Transition {
 * 		{@literal @}Override
 * 		public void effect() {
 * 			Action.log("Transition effect");
 * 		}
 * 
 * 		{@literal @}Override
 * 		public boolean guard() {
 * 			SampleSignal sg = getSignal(SampleSignal.class); 
 * 			return sg.sampleParam == 0;
 * 		}
 * 	}
 * 
 * 	//...
 * 
 * }
 * </code>
 * </pre>
 * 
 * A state having its entry and exit actions defined.
 * 
 * <pre>
 * <code>
 * class SampleClass3 extends ModelClass {
 * 
 * 	//...
 * 
 * 	class SampleState extends State {
 * 		{@literal @}Override
 * 		public void entry() {
 * 			Action.log("Entry action");
 * 		}
 * 
 * 		{@literal @}Override
 * 		public void exit() {
 * 			Action.log("Exit action");
 * 		}
 * 	}
 * 
 * 	//...
 * 
 * }
 * </code>
 * </pre>
 *
 * A composite state with a choice pseudostate inside. When the state machine
 * enters the composite state, it changes to the choice pseudostate. If the
 * attribute <code>i</code> has a value of zero, the <code>FromChoice1</code>
 * transition will be chosen to <code>S1</code>, otherwise the
 * <code>FromChoice2</code> transition to <code>S2</code>.
 * 
 * <pre>
 * <code>
 * class SampleClass4 extends ModelClass {
 * 
 * 	//...
 * 
 * 	int i = //...
 * 
 * 	class SampleCompositeState extends CompositeState {
 * 		class Init extends Initial {}
 * 		class SampleChoice extends Choice {}
 * 		class S1 extends State {}
 * 		class S2 extends State {}
 * 
 * 		{@literal @From(Init.class) @To(SampleChoice.class)}
 * 		class Initialize extends Transition {}
 * 
 * 		{@literal @From(SampleChoice.class) @To(S1.class)}
 * 		class FromChoice1 extends Transition {
 * 			{@literal @}Override
 * 			public boolean guard() {
 * 				return i == 0;
 * 			}
 * 		}
 * 
 * 		{@literal @From(SampleChoice.class) @To(S2.class)}
 * 		class FromChoice2 extends Transition {
 * 			{@literal @}Override
 * 			public boolean guard() {
 * 				return Else();
 * 			}
 * 		}
 * 
 * 		//...
 * 
 * 	}
 * 
 * 	//...
 * 
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see Vertex
 * @see Pseudostate
 * @see Initial
 * @see Choice
 * @see State
 * @see CompositeState
 *
 */
public abstract class StateMachine extends InnerClassInstancesHolder implements
		ModelElement {

	/**
	 * Sole constructor of <code>StateMachine</code>.
	 */
	StateMachine() {
	}

	/**
	 * Base class for vertices in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> vertex
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Should not be used directly, use its subclasses instead.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed, use its subclasses</li>
	 * </ul>
	 *
	 * <p>
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 * @see State
	 * @see CompositeState
	 * @see Pseudostate
	 * @see Initial
	 * @see Choice
	 */
	public class Vertex implements ModelElement {

		/**
		 * Sole constructor of <code>Vertex</code>.
		 */
		Vertex() {
		}

		/**
		 * Overridable method to implement the entry action of this vertex.
		 * <p>
		 * Overriding methods may only contain action code. See the
		 * documentation of {@link Model} for details about the action language.
		 */
		public void entry() {
		}

		/**
		 * Overridable method to implement the exit action of this vertex.
		 * <p>
		 * Overriding methods may only contain action code. See the
		 * documentation of {@link Model} for details about the action language.
		 */
		public void exit() {
		}

		/**
		 * Gives this object the reference of the actual triggering signal.
		 * 
		 * @param s
		 *            the actual triggering signal
		 */
		void setSignal(Signal s) {
		}

		/**
		 * Returns a string identifier of the vertex represented by this
		 * object's dynamic type.
		 * 
		 * @return the identifying string
		 */
		String vertexIdentifier() {
			return getClass().getSimpleName();
		}

		@Override
		public String toString() {
			return "vertex:" + vertexIdentifier();
		}

	}

	/**
	 * Base class for pseudostates in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> pseudostate
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Should not be used directly, use its subclasses instead.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed, use its subclasses</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @author Gabor Ferenc Kovacs
	 * @see Initial
	 * @see Choice
	 * @see State
	 * @see Vertex
	 */
	public class Pseudostate extends Vertex {

		/**
		 * Sole constructor of <code>Pseudostate</code>.
		 */
		Pseudostate() {
		}

		/**
		 * A final empty method overriding the entry action of {@link Vertex}.
		 * As pseudostates have no entry or exit actions, their
		 * <code>entry</code> and <code>exit</code> methods must be empty.
		 */
		@Override
		public final void entry() {
		}

		/**
		 * A final empty method overriding the exit action of {@link Vertex}. As
		 * pseudostates have no entry or exit actions, their <code>entry</code>
		 * and <code>exit</code> methods must be empty.
		 */
		@Override
		public final void exit() {
		}

		@Override
		public String toString() {
			return "pseudostate:" + vertexIdentifier();
		}

	}

	/**
	 * Base class for initial pseudostates in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> initial pseudostate
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Define subclasses of this class as inner classes of a subclass of
	 * <code>StateMachine</code> or <code>CompositeState</code>. These
	 * subclasses (only one for every <code>StateMachine</code> or
	 * <code>CompositeState</code>) will represent the initial pseudostates of
	 * those state machines or composite states.
	 * <p>
	 * An initial pseudostate may not be the target of any transitions and has
	 * to be the source of exactly one transition, which must not have any
	 * {@link Trigger} annotations or guards defined.
	 * <p>
	 * A state machine will step forward from its initial pseudostate when its
	 * container model class becomes {@link ModelClass.Status#ACTIVE ACTIVE} as
	 * the result of an {@link Action#start(ModelClass) Action.start} method
	 * call.
	 * <p>
	 * After entering a composite state, the state machine will step forward
	 * from its initial pseudostate as the result of an implicit synchronous
	 * event.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a subclass of {@link StateMachine} or
	 * {@link CompositeState}</li>
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Initial extends Pseudostate {

		/**
		 * Sole constructor of <code>Initial</code>.
		 */
		protected Initial() {
		}

		@Override
		public String toString() {
			return "initial_" + super.toString();
		}

	}

	/**
	 * Base class for choice pseudostates in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> choice pseudostate
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Define subclasses of this class as inner classes of a subclass of
	 * <code>StateMachine</code> or <code>CompositeState</code>. These
	 * subclasses (only one for every <code>StateMachine</code> or
	 * <code>CompositeState</code>) will represent the choice pseudostates of
	 * those state machines or composite states.
	 * <p>
	 * A choice pseudostate might be the source and target of any number of
	 * transitions. Outgoing transitions must be unlabeled (that is, have no
	 * {@link Trigger} annotations). Any time the state machine enters a choice
	 * pseudostate, exactly one of the {@link Transition#guard guards} of the
	 * outgoing transitions should evaluate to <code>true</code>. The only
	 * exception is when one of the guards evaluate to an
	 * {@link Transition#Else Else} condition. See the documentation of the
	 * {@link Transition#guard guard} method for details.
	 * <p>
	 * The state machine leaves a choice pseudostate right after it enters that,
	 * without any delay.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a subclass of {@link StateMachine} or
	 * {@link CompositeState}</li>
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Choice extends Pseudostate {

		/**
		 * Sole constructor of <code>Choice</code>.
		 */
		protected Choice() {
		}

		@Override
		public String toString() {
			return "choice_" + super.toString();
		}

	}

	/**
	 * Base class for states in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> state
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Define subclasses of this class as inner classes of a subclass of
	 * <code>StateMachine</code> or <code>CompositeState</code>. These
	 * subclasses will represent the states of those state machines or composite
	 * states.
	 * <p>
	 * A state might be the target and source of any number of transitions.
	 * Every outgoing transition must be labeled, that is, must have a
	 * {@link Trigger} annotation.
	 * <p>
	 * A state machine will step forward from a state <i>a</i> if:
	 * <ul>
	 * <li>it receives a signal <i>s</i>,</li>
	 * <li>there is a transition <i>t</i> which's source is <i>a</i> or any of
	 * <i>a</i>'s enclosing composite states,</li>
	 * <li><i>s</i> or one of its supertypes matches the <code>Trigger</code>
	 * annotation of <i>t</i>.</li>
	 * </ul>
	 * If all the preceding conditions hold, the guard of <i>t</i> is evaluated.
	 * If it is evaluated to <code>true</code>, the state machine uses <i>t</i>
	 * to change to another vertex, exiting all inner states if <i>t</i> is from
	 * a composite state. Otherwise, it searches for another transition that can
	 * be used.
	 * <p>
	 * There might exist <b>at most one</b> such <i>t</i> at a time.
	 * <p>
	 * After entering a state, its {@link #entry} action will be executed.
	 * Before leaving, its {@link #exit} action is performed.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a subclass of {@link StateMachine} or
	 * {@link CompositeState}</li>
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class State extends Vertex {

		/**
		 * Before the entry or exit action of this state is called, this field
		 * must be set to be the actual signal receiving which triggered the
		 * transition.
		 */
		private Signal signal;

		/**
		 * Sole constructor of <code>State</code>.
		 */
		protected State() {
		}

		/**
		 * Returns the signal receiving which triggered the execution of the
		 * actually performed transition. The return value is casted to any
		 * desired type which is a subclass of <code>Signal</code>.
		 * <p>
		 * It is guaranteed for this method to return a non-null value
		 * successfully if the following conditions are met for the actually
		 * performed transition <i>t</i> and a signal <i>s</i>:
		 * <ul>
		 * <li>tr(<i>t</i>, <i>s</i>) (<i>t</i> is triggered by <i>s</i>)
		 * <b>and</b></li>
		 * <li>a cast to <i>s</i> is asked.</li>
		 * </ul>
		 * 
		 * For any transition <i>t</i> and signal <i>s</i>, tr(<i>t</i>,
		 * <i>s</i>) if either
		 * <ul>
		 * <li><i>t</i> has a {@link Trigger} annotation defined which's value
		 * is set to <i>s</i> or any of its supertypes, <b>or</b></li>
		 * <li><i>t</i> is from a choice pseudostate <i>c</i> and for all
		 * transitions <i>t'</i> to <i>c</i>: tr(<i>t'</i>, <i>s</i>)</li>
		 * </ul>
		 * 
		 * If there are no signals for which the above described conditions are
		 * met, this method should not be called on the actual transition.
		 * 
		 * @param <T>
		 *            the type to which the casting should be performed
		 * @param signalClass
		 *            the signal class to which the casting should be performed
		 * @return the signal receiving which triggered the execution (or the
		 *         call of the guard) of this transition
		 * @throws ClassCastException
		 *             if the cast might not be performed
		 */
		@SuppressWarnings("unchecked")
		protected final <T extends Signal> T getSignal(Class<T> signalClass) {
			return (T) signal;
		}

		@Override
		final void setSignal(Signal s) {
			signal = s;
		}

		@Override
		public String toString() {
			return "state:" + vertexIdentifier();
		}

	}

	/**
	 * Base class for composite states in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> composite state
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * As a composite state is a state, all requirements and restrictions hold
	 * to the usage of this class that are specified in the documentation of
	 * {@link State} if the opposite is not stated explicitly.
	 * <p>
	 * A composite state differs from a state in that it may contain other
	 * vertices (subclasses of {@link Vertex}) and transitions (subclasses of
	 * {@link Transition}) inside it, with an obligatory initial pseudostate and
	 * initial transition (like in the case of state machines; see the
	 * documentation of {@link Initial} for details). After entering a composite
	 * state and executing its entry action, the container state machine will
	 * automatically step forward from the initial pseudostate of this composite
	 * state.
	 * <p>
	 * The state machine may only leave a composite state through a transition
	 * that is from that composite state or any of its enclosing composite
	 * states. See the documentation of {@link State} for more information.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a subclass of {@link StateMachine} or
	 * {@link CompositeState}</li>
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
	 * <li><i>Nested classes:</i> allowed, only non-static and extending either
	 * {@link StateMachine.Vertex} or {@link StateMachine.Transition}</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class CompositeState extends State {

		/**
		 * Sole constructor of <code>CompositeState</code>.
		 */
		protected CompositeState() {
		}

		@Override
		public String toString() {
			return "composite_" + super.toString();
		}

	}

	static class TransitionBase implements ModelElement {

		/**
		 * In the {@link Transition#guard guard}s of transitions which are from
		 * {@link Choice} pseudostates, <i>else</i> conditions might be given,
		 * for the corresponding transitions to be used when all other
		 * potentially usable transitions' guards are evaluated to <i>true</i>.
		 * <p>
		 * To define an <i>else</i> condition, simply override the
		 * {@link Transition#guard guard} of a transition to return with the
		 * value returned by this method:
		 * 
		 * <pre>
		 * <code>
		 * {@literal @}Override
		 * public boolean guard() {
		 * 	return Else();
		 * }
		 * </code>
		 * </pre>
		 * 
		 * For the sake of the implementation, this method in fact returns
		 * nothing but throws a special exception instead, which might be caught
		 * then by the API. Therefore, it is important that this method is only
		 * called in an overridden <code>guard</code> method when the actual
		 * transition is from a choice pseudostate.
		 * 
		 * @return an <i>else</i> condition; in fact, this method always throws
		 *         a special exception which is caught by the API to represent
		 *         an <i>else</i> condition
		 * @throws ElseException
		 *             always, due to implementation issues; should <b>not be
		 *             caught</b> by user code.
		 * @see Transition#guard
		 */
		protected static final boolean Else() throws ElseException {
			throw new ElseException();
		}

	}

	/**
	 * Base class for transitions in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> transition
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Define subclasses of this class as inner classes of a subclass of
	 * <code>StateMachine</code> or <code>CompositeState</code>. These
	 * subclasses will represent the transitions of those state machines or
	 * composite states.
	 * <p>
	 * A transition must have a {@link From} and a {@link To} annotation to
	 * specify its source and target vertices. A {@link Trigger} annotation has
	 * to be applied if the transition is from a state or composite state and
	 * might not be applied if it is from a pseudostate (initial or choice).
	 * <p>
	 * A transition must connect two vertices that are on the same level of the
	 * hierarchical state machine, that is, their representing classes are the
	 * inner classes of the same <code>StateMachine</code> or
	 * <code>CompositeState</code> subclass as the transition itself.
	 * <p>
	 * An {@link #effect} of the transition might be defined which is executed
	 * when the state machine uses that transition. Its {@link #guard} is
	 * evaluated each time the state machine tries to use the transition, and
	 * only uses it if the <code>guard</code> is evaluated to <code>true</code>.
	 * See the documentation of those methods for details.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a subclass of {@link StateMachine} or
	 * {@link CompositeState}</li>
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Transition extends TransitionBase {

		/**
		 * An instance of the class representing this transition's source
		 * vertex.
		 */
		private final Vertex source;

		/**
		 * An instance of the class representing this transition's target
		 * vertex.
		 */
		private final Vertex target;

		/**
		 * Before this transition is executed or its guard is called, this field
		 * must be set to be the actual signal receiving which triggered the
		 * transition.
		 */
		private Signal signal;

		/**
		 * Sole constructor of <code>Transition</code>.
		 */
		protected Transition() {
			Class<? extends Transition> cls = getClass();

			From from = cls.getAnnotation(From.class);
			To to = cls.getAnnotation(To.class);

			if (from == null || to == null) {
				this.source = null;
				this.target = null;
			} else {
				this.source = getInnerClassInstance(from.value());
				this.target = getInnerClassInstance(to.value());
			}
		}

		/**
		 * Overridable method to implement the effect of this transition.
		 * <p>
		 * Called when this transition is chosen for execution, after calling
		 * all exit actions from left vertices and before calling any entry
		 * actions.
		 * <p>
		 * If the actual transition has a trigger defined, the
		 * {@link #getSignal(Class) getSignal} method can be used inside the
		 * overriding methods to get the triggering signal.
		 * <p>
		 * Overriding methods may only contain action code. See the
		 * documentation of {@link Model} for details about the action language.
		 */
		public void effect() {
		}

		/**
		 * Overridable method to implement the guard of this transition. Only
		 * called after checking that the actual transition is from the current
		 * vertex or one of its enclosing composite states, and it is triggered
		 * by the received signal. If the current vertex is a choice
		 * pseudostate, only transitions from the choice and not from its
		 * enclosing composite states might be used.
		 * <p>
		 * Might not be overridden in transition classes that are from initial
		 * pseudostates.
		 * <p>
		 * If overridden, the return value must never be <code>null</code>.
		 * <p>
		 * If the overriding method returns <b><code>false</code></b>, this
		 * transition will not be chosen to be executed.
		 * <p>
		 * If the overriding method returns <b><code>true</code></b>, this
		 * transition might be chosen to be executed. Only one such transition
		 * may exist at any time (for which the two conditions mentioned above,
		 * regarding the source vertex and the triggering signal, are also met).
		 * Two such transitions are allowed to exist in one special case:
		 * <ul>
		 * <li>the current vertex is a choice pseudostate,</li>
		 * <li>the source of both transitions is the choice pseodostate (other
		 * transitions are not taken into consideration in the case of a choice
		 * pseudostate),</li>
		 * <li>that transition's <code>guard</code> method returns an
		 * {@link #Else} condition,</li>
		 * <li>the other transition's <code>guard</code> method does not return
		 * an {@link #Else} condition.</li>
		 * </ul>
		 * <p>
		 * In this case, the second transition will be executed as the one with
		 * an else condition is executed only if no other transition might be
		 * used.
		 * <p>
		 * If the overriding method once returns an {@link #Else} condition, it
		 * should always do so.
		 * <p>
		 * If the actual transition has a trigger defined, the
		 * {@link #getSignal(Class) getSignal} method can be used inside the
		 * overriding methods to get the triggering signal.
		 * <p>
		 * Overriding methods may only contain a condition evaluation. See the
		 * documentation of {@link Model} for details about condition
		 * evaluations in the model.
		 * 
		 * @return <code>true</code> by default implementation
		 */
		public boolean guard() {
			return true;
		}

		/**
		 * Returns the signal receiving which triggered the execution (or the
		 * call of the guard) of this transition. The return value is casted to
		 * any desired type which is a subclass of <code>Signal</code>.
		 * <p>
		 * It is guaranteed for this method to return a non-null value
		 * successfully if the following conditions are met for the actual
		 * transition <i>t</i> and a signal <i>s</i>:
		 * <ul>
		 * <li>tr(<i>t</i>, <i>s</i>) (<i>t</i> is triggered by <i>s</i>)
		 * <b>and</b></li>
		 * <li>a cast to <i>s</i> is asked.</li>
		 * </ul>
		 * 
		 * For any transition <i>t</i> and signal <i>s</i>, tr(<i>t</i>,
		 * <i>s</i>) if either
		 * <ul>
		 * <li><i>t</i> has a {@link Trigger} annotation defined which's value
		 * is set to <i>s</i> or any of its supertypes, <b>or</b></li>
		 * <li><i>t</i> is from a choice pseudostate <i>c</i> and for all
		 * transitions <i>t'</i> to <i>c</i>: tr(<i>t'</i>, <i>s</i>)</li>
		 * </ul>
		 * 
		 * If there are no signals for which the above described conditions are
		 * met, this method should not be called on the actual transition.
		 * 
		 * @param <T>
		 *            the type to which the casting should be performed
		 * @param signalClass
		 *            the signal class to which the casting should be performed
		 * @return the signal receiving which triggered the execution (or the
		 *         call of the guard) of this transition
		 * @throws ClassCastException
		 *             if the cast might not be performed
		 */
		@SuppressWarnings("unchecked")
		protected final <T extends Signal> T getSignal(Class<T> signalClass) {
			return (T) signal;
		}

		/**
		 * Sets the <code>signal</code> field of this transition.
		 * <p>
		 * Before this transition is executed or its guard is called,
		 * <code>signal</code> must be set to be the actual signal receiving
		 * which triggered the transition.
		 * <p>
		 * If the transition has no triggers defined and is not from a choice
		 * pseudostate, the value of the <code>signal</code> field is unused.
		 * 
		 * @param s
		 *            the new value of the <code>signal</code> field
		 */
		final void setSignal(Signal s) {
			signal = s;
		}

		/**
		 * @return the held instance of the class representing this transition's
		 *         source vertex
		 */
		Vertex getSource() {
			return source;
		}

		/**
		 * @return the held instance of the class representing this transition's
		 *         target vertex
		 */
		Vertex getTarget() {
			return target;
		}

		/**
		 * Checks if the class <code>c</code> is the representing class of this
		 * transition's source vertex.
		 * 
		 * @param c
		 *            the class to check if it is the source of this transition
		 * @return <code>true</code> if this transition is from <code>c</code>,
		 *         <code>false</code> otherwise
		 */
		boolean isFromSource(Class<?> c) {
			return source == null ? false : c == source.getClass();
		}

		@Override
		public String toString() {
			return "transition:" + getClass().getSimpleName() + " ("
					+ source.toString() + " --> " + target.toString() + ")";
		}

	}

}
