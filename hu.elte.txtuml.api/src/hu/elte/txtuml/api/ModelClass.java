package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.AssociationsMap;
import hu.elte.txtuml.api.backend.problems.MultiplicityException;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
// import hu.elte.txtuml.importer.MethodImporter;
// import hu.elte.txtuml.importer.ModelImporter;
import hu.elte.txtuml.utils.InstanceCreator;

public class ModelClass extends Region implements ModelElement,
		ModelIdentifiedElement, LayoutNode {

	/*
	 * DESTROYED status is currently unreachable, FINALIZED is only by a class
	 * which has no state machine.
	 */
	private enum Status {
		READY, ACTIVE, FINALIZED, DESTROYED
	}

	private static Integer counter = 0;

	private Status status;
	private final String identifier;
	private final AssociationsMap associations = AssociationsMap.create();

	@Override
	public String getIdentifier() {
		return identifier;
	}

	protected ModelClass() {
		super();

		synchronized (counter) {
			this.identifier = "obj_" + counter++;
		}

		if (getCurrentState() == null) {
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
		ret.setOwnerId(this.getIdentifier());
		return ret;

	}

	@SuppressWarnings("unchecked")
	<T extends ModelClass, AE extends AssociationEnd<T>> void addToAssoc(
			Class<AE> otherEnd, T object) {

		try {
			associations.put(otherEnd, (AE) InstanceCreator
					.createInstanceWithGivenParams(otherEnd, (Object) null)
					.init(assocPrivate(otherEnd).typeKeepingAdd(object)));
		} catch (MultiplicityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	<T extends ModelClass, AE extends AssociationEnd<T>> void removeFromAssoc(
			Class<AE> otherEnd, T object) {

		associations.put(
				otherEnd,
				(AE) InstanceCreator.createInstanceWithGivenParams(otherEnd,
						(Object) null).init(
						assocPrivate(otherEnd).typeKeepingRemove(object)));

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

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getIdentifier();
	}

}
