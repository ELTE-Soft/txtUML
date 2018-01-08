package hu.elte.txtuml.api.model.external;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.GeneralCollection.NonUnique;
import hu.elte.txtuml.api.model.GeneralCollection.Ordered;
import hu.elte.txtuml.api.model.GeneralCollection.Unique;
import hu.elte.txtuml.api.model.GeneralCollection.Unordered;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.OrderedAny;
import hu.elte.txtuml.api.model.OrderedUniqueAny;
import hu.elte.txtuml.api.model.UniqueAny;

/**
 * A helper class that provides runtime utilities and information about JtxtUML
 * API collections.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("rawtypes")
public final class Collections {

	private Collections() {
	}

	/**
	 * Returns the unbounded version of the given txtUML API collection type
	 * (which has the same ordering and uniqueness properties as the specified
	 * one).
	 */
	@SuppressWarnings("unchecked")
	public static <E> Class<? extends GeneralCollection<E>> unbound(Class<? extends GeneralCollection<E>> type) {
		boolean isOrdered = isOrdered(type);
		boolean isUnique = isUnique(type);

		Class<? extends GeneralCollection> result;

		if (isOrdered) {
			if (isUnique) {
				result = OrderedUniqueAny.class;
			} else {
				result = OrderedAny.class;
			}
		} else {
			if (isUnique) {
				result = UniqueAny.class;
			} else {
				result = Any.class;
			}
		}
		return (Class<? extends GeneralCollection<E>>) result;
	}

	/**
	 * Tells whether the given txtUML API collection type is ordered or not.
	 * 
	 * @see GeneralCollection#isOrdered()
	 */
	public static boolean isOrdered(Class<? extends GeneralCollection> type) {
		return Ordered.class.isAssignableFrom(type);
	}

	/**
	 * Tells whether the given txtUML API collection type is unordered or not.
	 * 
	 * @see GeneralCollection#isOrdered()
	 */
	public static boolean isUnordered(Class<? extends GeneralCollection> type) {
		return Unordered.class.isAssignableFrom(type);
	}

	/**
	 * Tells whether the given txtUML API collection type is a unique collection
	 * or not.
	 * 
	 * @see GeneralCollection#isUnique()
	 */
	public static boolean isUnique(Class<? extends GeneralCollection> type) {
		return Unique.class.isAssignableFrom(type);
	}

	/**
	 * Tells whether the given txtUML API collection type is a non-unique
	 * collection or not.
	 * 
	 * @see GeneralCollection#isUnique()
	 */
	public static boolean isNonUnique(Class<? extends GeneralCollection> type) {
		return NonUnique.class.isAssignableFrom(type);
	}

	/**
	 * Returns the lower bound of the given collection type, that is, the
	 * minimum number of elements it may contain. It can be 0 or any positive
	 * integer.
	 */
	public static int getLowerBound(Class<? extends GeneralCollection> type) {
		Min min = type.getAnnotation(Min.class);
		return (min == null) ? 0 : min.value();
	}

	/**
	 * Returns the upper bound of the given collection type, that is, the
	 * maximum number of elements it may contain. It can be 0, any positive
	 * integer or equal to {@link GeneralCollection#INFINITE_BOUND}, which means
	 * that the collection has no upper bound.
	 */
	public static int getUpperBound(Class<? extends GeneralCollection> type) {
		Max max = type.getAnnotation(Max.class);
		return (max == null) ? GeneralCollection.INFINITE_BOUND : max.value();
	}

}
