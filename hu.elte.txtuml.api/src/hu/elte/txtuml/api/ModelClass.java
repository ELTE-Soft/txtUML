package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.AssociationsMap;
import hu.elte.txtuml.api.backend.logs.ErrorMessages;
import hu.elte.txtuml.api.backend.logs.WarningMessages;
import hu.elte.txtuml.api.backend.MultiplicityException;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.utils.InstanceCreator;

import java.util.concurrent.atomic.AtomicLong;

public class ModelClass extends Region implements ModelElement, LayoutNode {

	/*
	 * The life cycle of a model object consists of steps represented by this
	 * enumeration type. <p> See the documentation of the {@link
	 * hu.elte.txtuml.api} package to get an overview on modeling in txtUML.
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
		 * 
		 * @see Action#start(ModelClass)
		 */
		READY,
		/**
		 * This status of a <code>ModelClass</code> object indicates that the
		 * represented model object's state machine is currently running.
		 * <p>
		 * It may be reached by starting the state machine of this object
		 * manually.
		 *
		 * @see Action#start(ModelClass)
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
		 * 
		 * @see Action#delete(ModelClass)
		 */
		DELETED
	}

	/**
	 * A static counter to give different identifiers to each created model
	 * object instance.
	 */
	private static AtomicLong counter = new AtomicLong(0);

	/**
	 * The current status of this model object.
	 * 
	 * @see Status
	 */
	private Status status;

	/**
	 * A unique identifier of this object.
	 */
	private final String identifier;

	private final AssociationsMap associations = AssociationsMap.create();

	@Override
	public String getIdentifier() {
		return identifier;
	}

	protected ModelClass() {
		super();

		this.identifier = "obj_" + counter.addAndGet(1);

		if (getCurrentVertex() == null) {
			status = Status.FINALIZED;
		} else {
			status = Status.READY;
		}
	}

	public <T extends ModelClass, AE extends AssociationEnd<T> & hu.elte.txtuml.api.semantics.Navigability.Navigable> AE assoc(
			Class<AE> otherEnd) {

		return assocPrivate(otherEnd);

	}

	private <T extends ModelClass, AE extends AssociationEnd<T>> AE assocPrivate(
			Class<AE> otherEnd) {

		@SuppressWarnings("unchecked")
		AE ret = (AE) associations.get(otherEnd);
		if (ret == null) {
			ret = InstanceCreator.createInstance(otherEnd);
			associations.put(otherEnd, ret);
		}
		ret.setOwner(this);
		return ret;
	}

	<T extends ModelClass, AE extends AssociationEnd<T>> void addToAssoc(
			Class<AE> otherEnd, T object) throws MultiplicityException {

		associations.put(
				otherEnd,
				InstanceCreator.createInstanceWithGivenParams(otherEnd,
						(Object) null).init(
						assocPrivate(otherEnd).typeKeepingAdd(object)));

	}

	<T extends ModelClass, AE extends AssociationEnd<T>> void removeFromAssoc(
			Class<AE> otherEnd, T object) {

		associations.put(
				otherEnd,
				InstanceCreator.createInstanceWithGivenParams(otherEnd,
						(Object) null).init(
						assocPrivate(otherEnd).typeKeepingRemove(object)));

	}

	<T extends ModelClass, AE extends AssociationEnd<T>> boolean hasAssoc(
			Class<AE> otherEnd, T object) {

		AssociationEnd<?> actualOtherEnd = associations.get(otherEnd);
		return actualOtherEnd == null ? false : actualOtherEnd.contains(object)
				.getValue();
	}

	@Override
	void process(Signal signal) {
		if (isDeleted()) {
			ModelExecutor.executorErrorLog(WarningMessages
					.getSignalArrivedToDeletedObjectMessage(this));
			return;
		}
		super.process(signal);
	}

	void start() {
		if (status != Status.READY) {
			return;
		}
		send(null); // to move from initial state
		status = Status.ACTIVE;
	}

	void send(Signal signal) {
		ModelExecutor.send(this, signal);
	}

	/**
	 * Checks whether this model object is in a {@link Status#DELETED DELETED}
	 * status.
	 * 
	 * @return <code>true</code> if this model object is in a
	 *         <code>DELETED</code> status, <code>false</code> otherwise
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
		for (AssociationEnd<?> assocEnd : this.associations.values()) {
			if (!assocEnd.isEmpty().getValue()) {
				return false;
			}
		}
		return true;
	}

	void forceDelete() {
		if (!isDeletable()) {
			ModelExecutor.executorErrorLog(ErrorMessages
					.getObjectCannotBeDeletedMessage(this));
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
