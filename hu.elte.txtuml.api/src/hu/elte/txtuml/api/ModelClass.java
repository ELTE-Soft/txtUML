package hu.elte.txtuml.api;

import java.util.concurrent.atomic.AtomicLong;

import hu.elte.txtuml.api.backend.collections.AssociationsMap;
import hu.elte.txtuml.api.backend.problems.MultiplicityException;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.utils.InstanceCreator;

public class ModelClass extends Region implements ModelElement,
		ModelIdentifiedElement, LayoutNode {

	private enum Status {
		READY, ACTIVE, FINALIZED, DELETED
	}

	private static AtomicLong counter = new AtomicLong(0);

	private Status status;
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
		return actualOtherEnd == null ? false : actualOtherEnd.contains(object).getValue();
	}
	
	@Override
	void process(Signal signal) {
		if (isDeleted()) {
			ModelExecutor.executorErrorLog("Warning: signal arrived to deleted model object " + toString());
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


	boolean isDeleted() {
		return status == Status.DELETED;
	}

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
			ModelExecutor.executorErrorLog("Error: model object " + toString()
					+ " cannot be deleted because of existing associations.");
			return;
		}

		status = Status.DELETED;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getIdentifier();
	}
	
}
