package hu.elte.txtuml.api.model.execution.impl.assoc;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.error.LowerBoundError;
import hu.elte.txtuml.api.model.error.UpperBoundError;
import hu.elte.txtuml.api.model.external.Associations;
import hu.elte.txtuml.api.model.external.Collections;
import hu.elte.txtuml.api.model.impl.Wrapper;
import hu.elte.txtuml.utils.InstanceCreator;

public interface AssociationEndRuntime<T extends ModelClass, C extends GeneralCollection<T>>
		extends Wrapper<AssociationEnd<C>> {

	GeneralCollection<T> getCollection();

	C getCollectionOfTargetType() throws LowerBoundError;

	void add(T object) throws MultiplicityException;

	void remove(T object);

	boolean checkLowerBound();

	default boolean isEmpty() {
		return getCollection().isEmpty();
	}

	// create methods

	static <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndRuntime<T, C> create(
			Class<? extends AssociationEnd<C>> typeOfWrapped) {
		return create(InstanceCreator.create(typeOfWrapped, (Object) null));
	}

	static <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndRuntime<T, C> create(
			AssociationEnd<C> wrapped) {

		final Class<C> type = Associations.getCollectionTypeOf(wrapped);

		return new AssociationEndRuntime<T, C>() {

			/**
			 * Is of the proper type if {@link #valid} is true; is of its
			 * unbounded version otherwise.
			 */
			private GeneralCollection<T> collection;

			private boolean valid = true;

			{
				// initialize collection
				try {
					collection = Action.collectIn(type);
				} catch (LowerBoundError e) {
					valid = false;
					collection = Action.collectIn(Collections.unbound(type));
				}
			}

			@Override
			public AssociationEnd<C> getWrapped() {
				return wrapped;
			}

			@Override
			public boolean checkLowerBound() {
				return valid;
			}

			@Override
			public GeneralCollection<T> getCollection() {
				return collection;
			}

			@SuppressWarnings("unchecked")
			@Override
			public C getCollectionOfTargetType() throws LowerBoundError {
				if (valid) {
					return (C) collection;
				} else {
					throw new LowerBoundError(type);
				}
			}

			@Override
			public void remove(T object) {
				try {
					collection = collection.remove(object);
				} catch (LowerBoundError e) {
					valid = false;
					collection = collection.unbound().remove(object);
				}
			}

			@Override
			public void add(T object) throws MultiplicityException {
				try {
					collection = collection.add(object);
				} catch (UpperBoundError e) {
					throw new MultiplicityException();
				}
				if (!valid) {
					try {
						collection = collection.as(type);
						valid = true; // If no exception is thrown.
					} catch (LowerBoundError e) {
						// Nothing to do.
					}
				}
			}

			@Override
			public String toString() {
				return "association_end<" + collection.toString() + ">";
			}

		};
	}

}
