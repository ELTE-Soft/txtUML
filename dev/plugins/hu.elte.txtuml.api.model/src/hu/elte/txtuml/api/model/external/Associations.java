package hu.elte.txtuml.api.model.external;

import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Container;
import hu.elte.txtuml.api.model.ZeroToOne;

/**
 * A helper class that provides runtime utilities and information about
 * associations and association ends.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public final class Associations {

	private Associations() {
	}

	/**
	 * Returns the collection type of the given association end, that is, the
	 * type of the collections that contain the objects at this association end.
	 */
	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOf(AssociationEnd<C> assocEnd) {
		if (assocEnd instanceof Container) {
			return getCollectionTypeOfContainerEnd();
		} else {
			return getCollectionTypeOfNonContainerEnd((Class<? extends AssociationEnd<C>>) assocEnd.getClass());
		}
	}

	/**
	 * Returns the collection type of the given association end type, that is,
	 * the type of the collections that contain the objects at this association
	 * end type.
	 */
	public static <C> Class<C> getCollectionTypeOf(Class<? extends AssociationEnd<C>> assocEndType) {
		if (Container.class.isAssignableFrom(assocEndType)) {
			return getCollectionTypeOfContainerEnd();
		} else {
			return getCollectionTypeOfNonContainerEnd(assocEndType);
		}
	}

	/**
	 * Returns the collection type of any container end, that is, the type of
	 * the collections that contain the objects at a container end.
	 */
	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOfContainerEnd() {
		return (Class<C>) ZeroToOne.class;
	}

	/**
	 * Returns the collection type of the given non-container association end
	 * type, that is, the type of the collections that contain the objects at
	 * this association end type.
	 */
	@SuppressWarnings("unchecked")
	public static <C> Class<C> getCollectionTypeOfNonContainerEnd(Class<? extends AssociationEnd<C>> assocEndType) {
		return (Class<C>) ((ParameterizedType) ((ParameterizedType) assocEndType.getGenericSuperclass())
				.getActualTypeArguments()[0]).getRawType();
	}

}
