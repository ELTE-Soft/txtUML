package hu.elte.txtuml.api.model.utils;

import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Container;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ZeroToOne;

/**
 * A helper class that provides runtime utilities and information about
 * associations and association ends.
 * <p>
 * Methods of this class cannot be used in the model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public final class Associations {

	private Associations() {
	}

	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOf(AssociationEnd<C> assocEnd) {
		if (assocEnd instanceof Container) {
			return getCollectionTypeOfContainerEnd();
		} else {
			return getCollectionTypeOfNonContainerEnd((Class<? extends AssociationEnd<C>>) assocEnd.getClass());
		}
	}

	public static <C> Class<C> getCollectionTypeOf(Class<? extends AssociationEnd<C>> assocEndType) {
		if (Container.class.isAssignableFrom(assocEndType)) {
			return getCollectionTypeOfContainerEnd();
		} else {
			return getCollectionTypeOfNonContainerEnd(assocEndType);
		}
	}

	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOfContainerEnd() {
		return (Class<C>) ZeroToOne.class;
	}

	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOfNonContainerEnd(Class<? extends AssociationEnd<C>> assocEndType) {
		ParameterizedType endType = (ParameterizedType) assocEndType.getGenericSuperclass();
		ParameterizedType collectionType = (ParameterizedType) endType.getActualTypeArguments()[0];

		return (Class<C>) collectionType.getRawType();
	}

	@SuppressWarnings("unchecked")
	public static <T, C extends GeneralCollection<T>> Class<T> getElementTypeOf(AssociationEnd<C> assocEnd) {
		return getElementTypeOf((Class<? extends AssociationEnd<C>>) assocEnd.getClass());
	}

	public static <T, C extends GeneralCollection<T>> Class<T> getElementTypeOf(
			Class<? extends AssociationEnd<C>> assocEndType) {
		if (Container.class.isAssignableFrom(assocEndType)) {
			return getElementTypeOfContainerEnd(assocEndType);
		} else {
			return getElementTypeOfNonContainerEnd(assocEndType);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, C extends GeneralCollection<T>> Class<T> getElementTypeOfContainerEnd(
			Class<? extends AssociationEnd<C>> assocEndType) {
		ParameterizedType endType = (ParameterizedType) assocEndType.getGenericSuperclass();

		return (Class<T>) endType.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public static <T, C extends GeneralCollection<T>> Class<T> getElementTypeOfNonContainerEnd(
			Class<? extends AssociationEnd<C>> assocEndType) {
		ParameterizedType endType = (ParameterizedType) assocEndType.getGenericSuperclass();
		ParameterizedType collectionType = (ParameterizedType) endType.getActualTypeArguments()[0];

		return (Class<T>) collectionType.getActualTypeArguments()[0];
	}

}
