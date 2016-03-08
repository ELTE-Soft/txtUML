package hu.elte.txtuml.api.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import hu.elte.txtuml.api.model.ModelExecutor.Report;

/**
 * Base class for ports in the model.
 * 
 * <p>
 * <b>Represents:</b> port
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Define connectors to specify the connections between ports and create these
 * connections at runtime with the appropriate action methods. Signals can be
 * sent through the receptions on the provided interface of a port with the
 * {@link Action#send(Signal, Reception)} method.
 * <p>
 * The {@link #provided} field of a port contains an instance of the provided
 * interface.
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
 * @param <R>
 *            the required interface
 * @param <P>
 *            the provided interface
 */
public abstract class Port<R extends Interface, P extends Interface> {

	/**
	 * The provided interface of this port.
	 * <p>
	 * This instance of the specified provided interface of this port instance
	 * can be used to reference the receptions on which a send operation might
	 * be performed.
	 * 
	 * @see Action#send(Signal, Reception)
	 */
	public final P provided;

	private Port<?, ?> neighbor1 = null;
	private Port<?, ?> neighbor2 = null;
	private ModelClass obj = null;

	protected Port() {
		this(1);
	}

	/**
	 * @param indexOfProvidedInterface
	 *            the index of the provided interface in the list of type
	 *            arguments
	 */
	@SuppressWarnings("unchecked")
	Port(int indexOfProvidedInterface) {
		Class<?> type = getClass();

		Class<P> typeOfProvided = (Class<P>) ((ParameterizedType) type.getGenericSuperclass())
				.getActualTypeArguments()[indexOfProvidedInterface];

		provided = (P) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { typeOfProvided },
				createReceptionHandler());
	}

	Port(P provided) {
		this.provided = provided;
	}

	private InvocationHandler createReceptionHandler() {
		return (Object proxy, Method method, Object[] args) -> {
			Port.this.accept((Signal) args[0], null);

			return null; // the actual method has to be void
		};
	}

	/**
	 * This port accepts the given signal from the specified sender. If sender
	 * is {@code null}, it means that this signal has been sent from a state
	 * machine or operation to this port.
	 */
	private void accept(Signal signal, Object sender) {
		// Exactly one of neighbor2 and obj should be null.
		if (sender == neighbor1) {
			if (neighbor2 != null) {
				neighbor2.accept(signal, this);
				return;
			}

			if (obj != null) {
				ModelExecutor.send(obj, this, signal);
				return;
			}
		} else {
			if (neighbor1 != null) {
				neighbor1.accept(signal, this);
				return;
			}
		}

		Report.warning.forEach(x -> x.lostSignalAtPort(this, signal));
	}

	void connectToPort(Port<?, ?> other) {
		if (neighbor1 == null) {
			neighbor1 = other;
		} else {
			neighbor2 = other;
		}
	}

	void connectToSM(ModelClass obj) {
		this.obj = obj;
	}

}