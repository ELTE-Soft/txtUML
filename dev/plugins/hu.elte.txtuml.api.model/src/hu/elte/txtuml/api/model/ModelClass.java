package hu.elte.txtuml.api.model;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import hu.elte.txtuml.api.model.ModelExecutor.Report;
import hu.elte.txtuml.api.model.assocends.ContainmentKind;
import hu.elte.txtuml.api.model.assocends.Navigability;
import hu.elte.txtuml.api.model.backend.MultipleContainerException;
import hu.elte.txtuml.api.model.backend.MultiplicityException;
import hu.elte.txtuml.api.model.backend.collections.AssociationsMap;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * Base class for classes in the model.
 * 
 * <p>
 * <b>Represents:</b> class
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
 * subclasses of <code>ModelClass</code> or primitives (including {@code String}
 * )</li>
 * <li><i>Initialization blocks:</i> allowed, containing only simple assignments
 * to set the default values of its fields</li>
 * <li><i>Fields:</i> allowed, only of types which are subclasses of
 * <code>ModelClass</code> or primitives (including {@code String}); they
 * represent attributes of the model class</li>
 * <li><i>Methods:</i> allowed, only with parameters and return values of types
 * which are subclasses of <code>ModelClass</code> or primitives (including
 * {@code String}); they represent operations of the model class</li>
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
public class ModelClass extends Region {

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
	public enum Status {
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object's state machine is not yet started. It will
		 * not react to any asynchronous events, for example, sending signals to
		 * it. However, sending signal to a <code>READY</code> object is legal
		 * in the model, therefore no error or warning messages are shown if it
		 * is done.
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
		 * in the model, therefore no error or warning messages are shown if it
		 * is done.
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
	 * A static counter to give different identifiers to each created model
	 * object instance.
	 */
	private static final AtomicLong counter = new AtomicLong(0);

	/**
	 * The current status of this model object.
	 */
	private volatile Status status;

	/**
	 * A unique identifier of this object.
	 */
	private final long identifier;

	/**
	 * A map of the associations of this model object.
	 */
	private final AssociationsMap associations = AssociationsMap.create();

	private final ClassToInstanceMap<Port<?, ?>> ports = MutableClassToInstanceMap.create();

	/**
	 * Sole constructor of <code>ModelClass</code>. Creates the unique
	 * identifier of this object and after setting its current vertex to its
	 * initial pseudostate (if any), it goes into either {@link Status#READY
	 * READY} or {@link Status#FINALIZED FINALIZED} status depending on whether
	 * it has any state machine or not (any initial pseudostate or not).
	 */
	protected ModelClass() {
		super();

		this.identifier = counter.incrementAndGet();

		if (getCurrentVertex() == null) {
			status = Status.FINALIZED;
		} else {
			status = Status.READY;
		}
	}

	/**
	 * Returns a unique identifier of this model object which is created upon
	 * the creation of this object.
	 * 
	 * @return the unique identifier of this model object
	 */
	@Override
	public final String getIdentifier() {
		return "obj_" + identifier;
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
	public <T extends ModelClass, C, AE extends AssociationEnd<T, C> & Navigability.Navigable> C assoc(
			Class<AE> otherEnd) {

		return assocPrivate(otherEnd).getCollection();

	}

	/**
	 * Gets the collection containing the objects in association with this
	 * object and being on the specified opposite (navigable or non-navigable)
	 * association end.
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
	<T extends ModelClass, C, AE extends AssociationEnd<T, C>> AE assocPrivate(Class<AE> otherEnd) {

		@SuppressWarnings("unchecked")
		AE ret = (AE) associations.get(otherEnd);
		if (ret == null) {
			ret = InstanceCreator.create(otherEnd, (Object) null);
			associations.put(otherEnd, ret);
		}
		return ret;
	}

	/**
	 * Adds the specified object to the collection containing the objects in
	 * association with this object and being on the specified opposite
	 * association end.
	 * 
	 * @param <T>
	 *            the type of objects which are on the opposite association end
	 * @param <AE>
	 *            the type of the opposite association end
	 * @param otherEnd
	 *            the opposite association end
	 * @param object
	 *            the object to add to the collection (should not be
	 *            <code>null</code>)
	 * @throws MultiplicityException
	 *             if the upper bound of the multiplicity of the opposite
	 *             association end is offended
	 * @throws MultipleContainerException
	 *             if the same object would be part of two containers
	 */
	<T extends ModelClass, C, AE extends AssociationEnd<T, C>> void addToAssoc(Class<AE> otherEnd, T object)
			throws MultiplicityException, MultipleContainerException {
		containerCheck(otherEnd);
		assocPrivate(otherEnd).add(object);
	}

	private <T extends ModelClass, C, AE extends AssociationEnd<T, C>> void containerCheck(Class<AE> otherEnd)
			throws MultipleContainerException {
		if (ContainmentKind.ContainerEnd.class.isAssignableFrom(otherEnd)) {
			for (Entry<Class<? extends AssociationEnd<?, ?>>, AssociationEnd<?, ?>> entry : associations.entrySet()) {
				if (ContainmentKind.ContainerEnd.class.isAssignableFrom(entry.getKey()) && !entry.getValue().isEmpty()) {
					throw new MultipleContainerException();
				}
			}
		}
	}

	/**
	 * Removes the specified object from the collection containing the objects
	 * in association with this object and being on the specified opposite
	 * association end.
	 * 
	 * @param <T>
	 *            the type of objects which are on the opposite association end
	 * @param <AE>
	 *            the type of the opposite association end
	 * @param otherEnd
	 *            the opposite association end
	 * @param object
	 *            the object to remove from the collection
	 */
	<T extends ModelClass, C, AE extends AssociationEnd<T, C>> void removeFromAssoc(Class<AE> otherEnd, T object) {

		AE assocEnd = assocPrivate(otherEnd);
		assocEnd.remove(object);

		if (ModelExecutor.Settings.dynamicChecks() && !assocEnd.checkLowerBound()) {
			ModelExecutor.checkLowerBoundInNextExecutionStep(this, otherEnd);
		}

	}

	/**
	 * Checks if the specified object is element of the collection containing
	 * the objects in association with this object and being on the specified
	 * opposite association end.
	 * 
	 * @param <T>
	 *            the type of objects which are on the opposite association end
	 * @param <AE>
	 *            the type of the opposite association end
	 * @param otherEnd
	 *            the opposite association end
	 * @param object
	 *            the object to check in the collection
	 * @return <code>true</code> if <code>object</code> is included in the
	 *         collection related to <code>otherEnd</code>, <code>false</code>
	 *         otherwise
	 */
	<T extends ModelClass, AE extends AssociationEnd<T, ?>> boolean hasAssoc(Class<AE> otherEnd, T object) {

		@SuppressWarnings("unchecked")
		AssociationEnd<T, ?> actualOtherEnd = (AssociationEnd<T, ?>) associations.get(otherEnd);
		return actualOtherEnd == null ? false : actualOtherEnd.contains(object);
	}

	/**
	 * Checks the lower bound of the specified association end's multiplicity.
	 * Shows a message in case of an error. If this object is in
	 * {@link Status#DELETED DELETED} status, the check is ignored.
	 * 
	 * @param assocEnd
	 *            the association end to check
	 */
	void checkLowerBound(Class<? extends AssociationEnd<?, ?>> assocEnd) {

		if (status != Status.DELETED && !assocPrivate(assocEnd).checkLowerBound()) {
			Report.error.forEach(x -> x.lowerBoundOfMultiplicityOffended(this, assocEnd));
		}

	}

	/**
	 * Gets the instance of the specified port type on this model object.
	 * 
	 * @param portType
	 *            a specific port type which has to be a port type on this model
	 *            class
	 * @return the instance of the specified port type
	 */
	public <P extends Port<?, ?>> P port(Class<P> portType) {
		P inst = ports.getInstance(portType);

		if (inst == null) {
			inst = InstanceCreator.create(portType, this);
			if (portType.isAnnotationPresent(BehaviorPort.class)) {
				inst.connectToSM(this);
			}
			ports.putInstance(portType, inst);
		}

		return inst;
	}

	@Override
	void process(Port<?, ?> port, Signal signal) {
		if (isDeleted()) {
			Report.warning.forEach(x -> x.signalArrivedToDeletedObject(this, signal));
			return;
		}
		super.process(port, signal);
	}

	/**
	 * Starts the state machine of this object.
	 * <p>
	 * If this object is <i>not</i> in {@link Status#READY READY} status, this
	 * method does nothing. Otherwise, it sends an asynchronous request to
	 * itself to step forward from its initial pseudostate and also changes its
	 * status to {@link Status#ACTIVE ACTIVE}.
	 * <p>
	 * If the optional dynamic checks are switched on in
	 * {@link ModelExecutor.Settings}, this method also initializes the defined
	 * association ends of this model object by calling the
	 * {@link #initializeAllDefinedAssociationEnds} method.
	 */
	@Override
	void start() {
		if (status != Status.READY) {
			return;
		}
		status = Status.ACTIVE;

		if (ModelExecutor.Settings.dynamicChecks()) {
			initializeAllDefinedAssociationEnds();
		}

		super.start();
	}

	/**
	 * Looks up all the defined associations of the model class this object is
	 * an instance of and initializes them, if they have not been initialized
	 * yet, by assigning empty {@link Collection Collections} to them in the
	 * {@link #associations} map. If any of them has a lower bound which is
	 * currently offended then registers that association end to be checked in
	 * the next <i>execution step</i>.
	 * <p>
	 * Shows an error about a bad model if any exception is thrown during the
	 * above described process as this method and all the methods this calls,
	 * assume that the model is well-defined.
	 * <p>
	 * See the documentation of {@link Model} for information about execution
	 * steps.
	 */
	private void initializeAllDefinedAssociationEnds() {
		// TODO Implement initialization of defined association ends.
	}

	/**
	 * Checks whether this model object is in {@link Status#DELETED DELETED}
	 * status.
	 * 
	 * @return <code>true</code> if this model object is in <code>DELETED</code>
	 *         status, <code>false</code> otherwise
	 */
	boolean isDeleted() {
		return status == Status.DELETED;
	}

	/**
	 * Checks if this model object is ready to be deleted. If it is already
	 * deleted, this method automatically returns <code>true</code>. Otherwise,
	 * it checks whether all associations to this object have already been
	 * unlinked.
	 * 
	 * @return <code>true</code> if this model object is ready to be deleted,
	 *         <code>false</code> otherwise
	 */
	boolean isDeletable() {
		if (isDeleted()) {
			return true;
		}
		for (AssociationEnd<?, ?> assocEnd : this.associations.values()) {
			if (!assocEnd.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Deletes the specified model object.
	 * <p>
	 * Might only be called if all associations of the specified model object
	 * are already unlinked. Shows an error otherwise.
	 * <p>
	 * See {@link ModelClass.Status#DELETED DELETED} status of model objects for
	 * more information about model object deletion.
	 */
	void forceDelete() {
		if (!isDeletable()) {
			Report.error.forEach(x -> x.objectCannotBeDeleted(this));
			return;
		}

		this.inactivate();

		status = Status.DELETED;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getIdentifier();
	}

}
