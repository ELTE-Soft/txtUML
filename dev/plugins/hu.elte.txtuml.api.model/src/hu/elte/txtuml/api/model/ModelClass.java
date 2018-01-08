package hu.elte.txtuml.api.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.ImplRelated.RequiresRuntime;
import hu.elte.txtuml.api.model.error.PortParameterError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.api.model.impl.ModelClassRuntime;
import hu.elte.txtuml.api.model.impl.ModelRuntime;
import hu.elte.txtuml.api.model.impl.PortRuntime;

/**
 * Base class for classes in the model.
 * 
 * <p>
 * <b>Represents:</b> model class
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Inherit from this class to define classes of the model. Fields of the
 * subclass will represent attributes of the model class, methods will represent
 * operations, while inheritance between subclasses of <code>ModelClass</code>
 * will represent inheritance in the model. That means, due to the restrictions
 * of Java, each model class may have at most one base class.
 * <p>
 * See the documentation of {@link StateMachine} about applying state machines
 * to model classes.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> allowed, only with parameters of types which are
 * subclasses of <code>ModelClass</code>, signals, data types, model enums or
 * primitives (including {@code String})</li>
 * <li><i>Initialization blocks:</i> allowed, containing only simple assignments
 * to set the default values of its fields</li>
 * <li><i>Fields:</i> allowed, only with parameters of types which are data
 * types, model enums or primitives (including {@code String} ); they represent
 * attributes of the model class</li>
 * <li><i>Methods:</i> allowed, only with parameters and return values of types
 * which are subclasses of <code>ModelClass</code>, signals, data types, model
 * enums or primitives (including {@code String}); they represent operations of
 * the model class</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> allowed, only non-static and extending either
 * {@link StateMachine.Vertex} or {@link StateMachine.Transition}</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed, to represent class
 * inheritance</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class Employee extends ModelClass {
 * 
 * 	String name;
 * 
 * 	int id;
 * 
 * 	void work(int hours, int payment) {
 * 		{@literal //...}
 * 	}
 *  
 * 	{@literal //...}
 *  
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link StateMachine} for detailed examples about
 * defining state machines.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class ModelClass extends StateMachine {

	/**
	 * The life cycle of a model object consists of steps represented by the
	 * constants of this enumeration type.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @see Status#READY
	 * @see Status#ACTIVE
	 * @see Status#FINALIZED
	 * @see Status#DELETED
	 */
	@External
	public enum Status {
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object's state machine is not yet started. It will
		 * not react to any asynchronous events, for example, sending signals to
		 * it. However, sending signal to a <code>READY</code> object is legal
		 * in the model, therefore no error messages are shown if it is done.
		 * <p>
		 * See the documentation of {@link Model} for an overview on modeling in
		 * JtxtUML.
		 * 
		 * @see Status#ACTIVE
		 */
		READY,
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object's state machine is currently running.
		 * <p>
		 * It may be reached by starting the state machine of this object
		 * manually with the {@link Action#start(ModelClass)} method.
		 * <p>
		 * See the documentation of {@link Model} for an overview on modeling in
		 * JtxtUML.
		 */
		ACTIVE,
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object either has no state machine or its state
		 * machine is already stopped but the object itself is not yet deleted
		 * from the model. Its fields and methods might be used but it will not
		 * react to any asynchronous events, for example, sending signals to it.
		 * However, sending signal to a <code>FINALIZED</code> object is legal
		 * in the model, therefore no error messages are shown if it is done.
		 * <p>
		 * <b>Note:</b> currently there is no way to stop the state machine of a
		 * model object without deleting it. So the only way to reach this
		 * status is to implement a model class without a state machine.
		 * <p>
		 * See the documentation of {@link Model} for an overview on modeling in
		 * JtxtUML.
		 */
		FINALIZED,
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object is deleted. No further use of this object is
		 * allowed, however, using its fields or methods do not result in any
		 * error messages because of the limitations of the Java language.
		 * <p>
		 * An object may only be in this status when all of its associations are
		 * unlinked and its state machine is stopped.
		 * <p>
		 * See the documentation of {@link Model} for an overview on modeling in
		 * JtxtUML.
		 * 
		 * @see Action#delete(ModelClass)
		 */
		DELETED
	}

	/**
	 * Initializes this model object, after which it goes into either
	 * {@link Status#READY READY} or {@link Status#FINALIZED FINALIZED} status
	 * depending on whether it has any state machine or not (any initial
	 * pseudostate or not).
	 */
	@ExternalBody
	protected ModelClass() {
	}

	@Override
	@External
	ModelClassRuntime createRuntime() {
		return ModelRuntime.current().createModelClassRuntime(this);
	}

	/**
	 * Gets the collection containing the objects in association with this
	 * object and being on the specified opposite <i>navigable</i> association
	 * end. May not be called with a non-navigable association end as its
	 * parameter.
	 * 
	 * @param <T>
	 *            the type of objects which are on the opposite association end
	 * @param <AE>
	 *            the type of the opposite association end
	 * @param otherEnd
	 *            the opposite association end
	 * @return collection containing the objects in association with this object
	 *         and being on <code>otherEnd</code>
	 */
	@ExternalBody
	public final <T extends ModelClass, C extends GeneralCollection<T>, AE extends AssociationEnd<C> & Navigable> C assoc(
			Class<AE> otherEnd) {
		ExecutorThread.current().requireOwned(this);

		return runtime().navigateThroughAssociation(otherEnd);
	}

	/**
	 * Gets the instance of the specified port type on this model object.
	 * 
	 * @param portType
	 *            a specific port type which has to be a port type on this model
	 *            class
	 * @return the instance of the specified port type
	 */
	@ExternalBody
	public final <P extends Port<?, ?>> P port(Class<P> portType) {
		ExecutorThread.current().requireOwned(this);

		return runtime().getPortInstance(portType);
	}

	/**
	 * Base class for ports in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> port
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Define connectors to specify the connections between ports and create
	 * these connections at runtime with the appropriate action methods. Signals
	 * can be sent through the receptions on the required interface of a port
	 * with the {@link Action#send(Signal, Reception)} method.
	 * <p>
	 * The {@link #required} field of a port contains an instance of the
	 * required interface.
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
	 * @see BehaviorPort
	 * @see Connector
	 * @see Delegation
	 * @see Action#connect(Port, Class, Port)
	 * @see Action#connect(Class, Port, Class, Port)
	 * @see InPort
	 * @see OutPort
	 * 
	 * @param <P>
	 *            the provided interface
	 * @param <R>
	 *            the required interface
	 */
	public class Port<P extends Interface, R extends Interface>
			extends @External RequiresRuntime<PortRuntime> {

		/**
		 * The required interface of this port.
		 * <p>
		 * This instance of the specified required interface of this port
		 * instance can be used to reference the receptions on which a send
		 * operation might be performed.
		 * 
		 * @see Action#send(Signal, Reception)
		 */
		public final R required;

		@ExternalBody
		protected Port() {
			this(1);
		}

		@Override
		@External
		PortRuntime createRuntime() {
			return ModelClass.this.runtime().getModelRuntime().createPortRuntime(this, ModelClass.this);
		}

		/**
		 * @param indexOfProvidedInterface
		 *            the index of the provided interface in the list of type
		 *            arguments
		 */
		@SuppressWarnings("unchecked")
		@External
		Port(int indexOfRequiredInterface) {
			Class<?> type = getClass();

			try {
				Class<R> typeOfRequired = (Class<R>) ((ParameterizedType) type.getGenericSuperclass())
						.getActualTypeArguments()[indexOfRequiredInterface];

				required = (R) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { typeOfRequired },
						createReceptionHandler());
			} catch (Throwable t) {
				throw new PortParameterError(type);
			}
		}

		@External
		Port(R required) {
			this.required = required;
		}

		@External
		private InvocationHandler createReceptionHandler() {
			return (Object proxy, Method method, Object[] args) -> {
				runtime().receiveLater((Signal) args[0]);

				return null; // the actual method has to be void
			};
		}

	}

	/**
	 * A base class for ports with an empty provided interface. It is only a
	 * syntactic sugar in JtxtUML, the following two definitions are
	 * semantically equal:
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
	 * 
	 * and
	 * 
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
	 * @param <P>
	 *            the provided interface
	 */
	public abstract class InPort<P extends Interface> extends Port<P, Interface.Empty> {

		@ExternalBody
		protected InPort() {
			super(new Interface.Empty() {
			});
		}

	}

	/**
	 * A base class for ports with an empty required interface. It is only a
	 * syntactic sugar in JtxtUML, the following two definitions are
	 * semantically equal:
	 * 
	 * <pre>
	 * <code>
	 * class SampleClass extends ModelClass {
	 * 
	 * 	class SamplePort extends OutPort{@literal <SampleInterface>} {}
	 * 
	 * 	// ...
	 * }
	 * </code>
	 * </pre>
	 * 
	 * and
	 * 
	 * <pre>
	 * <code>
	 * class SampleClass extends ModelClass {
	 * 
	 * 	class SamplePort extends Port{@literal <Interface.Empty, SampleInterface>} {}
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
	public abstract class OutPort<R extends Interface> extends Port<Interface.Empty, R> {

		@ExternalBody
		protected OutPort() {
			super(0);
		}

	}

}
