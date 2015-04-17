package hu.elte.txtuml.api;

/**
 * TODO overview
 * 
 * <p>
 * <b>Represents:</b> state machine
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * TODO usage
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * TODO examples
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public abstract class StateMachine extends NestedClassInstancesHolder implements
		ModelElement {

	/**
	 * Sole constructor of <code>StateMachine</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is neither instantiated, nor
	 * directly inherited by the user.
	 */
	StateMachine() {
	}

	/**
	 * Base class for vertices in the model
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
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
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Package private to make sure that this class is neither instantiated,
		 * nor directly inherited by the user.
		 */
		Vertex() {
		}

		/**
		 * Overridable method to implement the entry action of this vertex.
		 * <p>
		 * Overriding methods may only contain action code. See the
		 * documentation of the {@link hu.elte.txtuml.api} package for details
		 * about the action language.
		 */
		public void entry() {
		}

		/**
		 * Overridable method to implement the exit action of this vertex.
		 * <p>
		 * Overriding methods may only contain action code. See the
		 * documentation of the {@link hu.elte.txtuml.api} package for details
		 * about the action language.
		 */
		public void exit() {
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
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
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
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Package private to make sure that this class is neither instantiated,
		 * nor directly inherited by the user.
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
	 * TODO overview
	 * 
	 * <p>
	 * <b>Represents:</b> initial pseudostate
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * TODO usage
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>TODO requirements</li>
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
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Initial extends Pseudostate {

		/**
		 * Sole constructor of <code>Initial</code>.
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Protected because this class is intended to be inherited from but not
		 * instantiated. However, <code>Initial</code> has to be a non-abstract
		 * class to make sure that it is instantiatable when that is needed for
		 * the API or the model exportation.
		 */
		protected Initial() {
		}

		@Override
		public String toString() {
			return "initial_" + super.toString();
		}

	}

	/**
	 * TODO overview
	 * 
	 * <p>
	 * <b>Represents:</b> choice pseudostate
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * TODO usage
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>TODO requirements</li>
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
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Choice extends Pseudostate {

		/**
		 * Sole constructor of <code>Choice</code>.
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Protected because this class is intended to be inherited from but not
		 * instantiated. However, <code>Choice</code> has to be a non-abstract
		 * class to make sure that it is instantiatable when that is needed for
		 * the API or the model exportation.
		 */
		protected Choice() {
		}

		@Override
		public String toString() {
			return "choice_" + super.toString();
		}

	}

	/**
	 * TODO overview
	 * 
	 * <p>
	 * <b>Represents:</b> state
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * TODO usage
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>TODO requirements</li>
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
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class State extends Vertex {

		/**
		 * Sole constructor of <code>State</code>.
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Protected because this class is intended to be inherited from but not
		 * instantiated. However, <code>State</code> has to be a non-abstract
		 * class to make sure that it is instantiatable when that is needed for
		 * the API or the model exportation.
		 */
		protected State() {
		}

		@Override
		public String toString() {
			return "state:" + vertexIdentifier();
		}

	}

	
	/**
	 * TODO overview
	 * 
	 * <p>
	 * <b>Represents:</b> composite state
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * TODO usage
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>TODO requirements</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b> TODO restrictions
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
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class CompositeState extends State {

		/**
		 * Sole constructor of <code>CompositeState</code>.
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Protected because this class is intended to be inherited from but not
		 * instantiated. However, <code>CompositeState</code> has to be a
		 * non-abstract class to make sure that it is instantiatable when that
		 * is needed for the API or the model exportation.
		 */
		protected CompositeState() {
		}

		@Override
		public String toString() {
			return "composite_" + super.toString();
		}

	}

	/**
	 * TODO overview
	 * 
	 * <p>
	 * <b>Represents:</b> transition
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * TODO usage
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>TODO requirements</li>
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
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li></li>
	 * </ul>
	 * 
	 * See the documentation of {@link StateMachine} for detailed examples.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public class Transition implements ModelElement {

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
		 * <p>
		 * If the transition has no triggers defined, the value of this field is
		 * unused.
		 */
		private Signal signal;

		/**
		 * Sole constructor of <code>Transition</code>.
		 * <p>
		 * <b>Implementation note:</b>
		 * <p>
		 * Protected because this class is intended to be inherited from but not
		 * instantiated. However, <code>Transition</code> has to be a
		 * non-abstract class to make sure that it is instantiatable when that
		 * is needed for the API or the model exportation.
		 */
		protected Transition() {
			Class<? extends Transition> cls = getClass();

			From from = cls.getAnnotation(From.class);
			To to = cls.getAnnotation(To.class);

			if (from == null || to == null) {
				this.source = null;
				this.target = null;
				// TODO show error
			} else {
				this.source = getNestedClassInstance(from.value());
				this.target = getNestedClassInstance(to.value());
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
		 * documentation of the {@link hu.elte.txtuml.api} package for details
		 * about the action language.
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
		 * If the overriding method returns a <code>ModelBool</code>
		 * representing <b><code>false</code></b>, this transition will not be
		 * chosen to be executed.
		 * <p>
		 * If the overriding method returns a <code>ModelBool</code>
		 * representing <b><code>true</code></b>, this transition might be
		 * chosen to be executed. Only one such transition may exist at any time
		 * (for which the two conditions mentioned above, regarding the source
		 * vertex and the triggering signal, are also met). Two such transitions
		 * are allowed to exist in one special case:
		 * <ul>
		 * <li>the current vertex is a choice pseudostate</li>
		 * <li>the source of both transitions is the choice pseodostate (other
		 * transitions are not taken into consideration in the case of a choice
		 * pseudostate)</li>
		 * <li>that transition's <code>guard</code> method returns an
		 * {@link ModelBool.Else Else} instance which always represents
		 * <code>true</code></li>
		 * <li>the other transition's <code>guard</code> method does not return
		 * an <code>Else</code> instance</li>
		 * </ul>
		 * In this case, the second transition will be executed as the one with
		 * an else condition is executed only if no other transition might be
		 * used.
		 * <p>
		 * If the overriding method once returns an <code>Else</code> instance,
		 * it should always do so.
		 * <p>
		 * If the actual transition has a trigger defined, the
		 * {@link #getSignal(Class) getSignal} method can be used inside the
		 * overriding methods to get the triggering signal.
		 * <p>
		 * Overriding methods may only contain a condition evaluation. See the
		 * documentation of the {@link hu.elte.txtuml.api} package for details
		 * about condition evaluations in the model.
		 * 
		 * @return a <code>ModelBool</code> representing <code>true</code> by
		 *         default implementation
		 */
		public ModelBool guard() {
			return ModelBool.TRUE;
		}

		/**
		 * Returns the signal receiving which triggered the execution (or the
		 * call of the guard) of this transition. The return value is casted to
		 * any desired type which is a subclass of <code>Signal</code>. <p> It
		 * is guaranteed for this method to return a non-null value successfully
		 * if the following conditions are met: <ul> <li>the actual transition
		 * has a trigger defined with a triggering signal <code>s</code></li>
		 * 
		 * <p> FIXME case of choice <p>
		 * 
		 * <li>the transition is not from an initial or a choice
		 * pseudostate</li> <li>a cast to <code>s</code> is asked</li> </ul> If
		 * the transition has no triggers defined, this method should not be
		 * called.
		 * 
		 * @param signalClass the signal class to which the casting should be
		 * performed
		 * @return the signal receiving which triggered the execution (or the
		 * call of the guard) of this transition
		 * @throws ClassCastException if the cast might not be performed
		 */
		@SuppressWarnings("unchecked")
		protected final <T extends Signal> T getSignal(Class<T> signalClass) {
			return (T) signal;
		}

		/**
		 * Sets the <code>signal</code> field of this transition. <p> Before
		 * this transition is executed or its guard is called,
		 * <code>signal</code> must be set to be the actual signal receiving
		 * which triggered the transition. <p> If the transition has no triggers
		 * defined, the value of the <code>signal</code> field is unused.
		 * 
		 * <p> FIXME case of choice <p>
		 * 
		 * @param s the new value of the <code>signal</code> field
		 */
		final void setSignal(Signal s) {
			signal = s;
		}

		/**
		 * @return the held instance of the class representing this transition's
		 * source vertex
		 */
		Vertex getSource() {
			return source;
		}

		/**
		 * @return the held instance of the class representing this transition's
		 * target vertex
		 */
		Vertex getTarget() {
			return target;
		}

		/**
		 * Checks if the class <code>c</code> is the representing class of this
		 * transition's source vertex.
		 * 
		 * @param c the class to check if it is the source of this transition
		 * @return <code>true</code> if this transition is from <code>c</code>,
		 * <code>false</code> otherwise
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
