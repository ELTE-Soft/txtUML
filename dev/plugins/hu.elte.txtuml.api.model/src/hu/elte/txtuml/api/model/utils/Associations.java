package hu.elte.txtuml.api.model.utils;

import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Container;
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
		return (Class<C>) ((ParameterizedType) ((ParameterizedType) assocEndType.getGenericSuperclass())
				.getActualTypeArguments()[0]).getRawType();
	}

}
