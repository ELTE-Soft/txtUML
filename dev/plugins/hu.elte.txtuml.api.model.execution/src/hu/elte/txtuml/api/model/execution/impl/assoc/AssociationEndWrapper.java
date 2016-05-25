package hu.elte.txtuml.api.model.execution.impl.assoc;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.runtime.Wrapper;
import hu.elte.txtuml.utils.InstanceCreator;

public interface AssociationEndWrapper<T extends ModelClass, C extends Collection<T>>
		extends Wrapper<AssociationEnd<T, C>> {

	C getCollection();

	void add(T object) throws MultiplicityException;

	void remove(T object);

	default boolean checkLowerBound() {
		return getWrapped().checkLowerBound(getCollection().count());
	}

	default boolean isEmpty() {
		return getCollection().isEmpty();
	}

	// create methods

	static <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> create(
			Class<? extends AssociationEnd<T, C>> typeOfWrapped) {
		return create(InstanceCreator.create(typeOfWrapped, (Object) null));
	}

	static <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> create(
			AssociationEnd<T, C> wrapped) {
		return new AssociationEndWrapper<T, C>() {

			private C collection = wrapped.createEmptyCollection();

			@Override
			public AssociationEnd<T, C> getWrapped() {
				return wrapped;
			}

			@Override
			public C getCollection() {
				return collection;
			}

			@Override
			@SuppressWarnings("unchecked")
			public void remove(T object) {
				collection = (C) collection.remove(object);
			}

			@Override
			@SuppressWarnings("unchecked")
			public void add(T object) throws MultiplicityException {
				if (!wrapped.checkUpperBound(collection.count() + 1)) {
					throw new MultiplicityException();
				}
				collection = (C) collection.add(object);
			}

			@Override
			public String toString() {
				return "association_end<" + collection.toString() + ">";
			}

		};
	}

}
