package hu.elte.txtuml.api.model.execution.impl.assoc;

import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.error.LowerBoundError;
import hu.elte.txtuml.api.model.error.MultiplicityError;
import hu.elte.txtuml.api.model.error.UpperBoundError;
import hu.elte.txtuml.api.model.runtime.Wrapper;
import hu.elte.txtuml.utils.InstanceCreator;

public interface AssociationEndWrapper<T extends ModelClass, C extends GeneralCollection<T>>
		extends Wrapper<AssociationEnd<C>> {

	C getCollection();

	void add(T object) throws MultiplicityException;

	void remove(T object);

	boolean checkLowerBound();

	default boolean isEmpty() {
		return getCollection().isEmpty();
	}

	// create methods

	static <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndWrapper<T, C> create(
			Class<? extends AssociationEnd<C>> typeOfWrapped) {
		return create(InstanceCreator.create(typeOfWrapped, (Object) null));
	}

	static <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndWrapper<T, C> create(
			AssociationEnd<C> wrapped) {

		// TODO error handling
		@SuppressWarnings("unchecked")
		final Class<C> type = (Class<C>) ((ParameterizedType) wrapped.getClass().getAnnotatedSuperclass())
				.getActualTypeArguments()[0];

		return new AssociationEndWrapper<T, C>() {

			private GeneralCollection<T> collection = Action.collectIn(type);
			private boolean valid = true;

			@Override
			public AssociationEnd<C> getWrapped() {
				return wrapped;
			}

			@Override
			public boolean checkLowerBound() {
				return valid;
			}

			@SuppressWarnings("unchecked")
			@Override
			public C getCollection() {
				if (valid) {
					return (C) collection;
				} else {
					// TODO error handling
					throw new Error();
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
					throw new MultiplicityError();
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
