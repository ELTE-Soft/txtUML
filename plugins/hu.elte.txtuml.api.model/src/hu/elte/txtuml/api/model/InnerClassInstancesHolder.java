package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.backend.collections.InnerClassInstancesMap;
import hu.elte.txtuml.utils.InstanceCreator;

/**
 * An abstract class which contains a mapping to single instances of its inner
 * classes. Can also have mappings to inner classes of its inner classes (in any
 * depth). A subclass of this class can have mappings to inner classes of any of
 * its superclasses.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
abstract class InnerClassInstancesHolder {

	/**
	 * The map which contains instances of the inner classes.
	 */
	private final InnerClassInstancesMap innerClassInstances = InnerClassInstancesMap
			.create();

	/**
	 * Sole constructor of <code>InnerClassInstancesHolder</code>. Puts itself
	 * inside the map which contains instances of the inner classes.
	 */
	InnerClassInstancesHolder() {
		super();

		this.innerClassInstances.put(getClass(), this);
	}

	/**
	 * Returns an instance of the specified class. If the map does not hold such
	 * an instance already, this method creates it with reflection and makes
	 * sure that it has a correct reference to the same instance of its
	 * enclosing class which is included in the map.
	 * <p>
	 * If the specified class is not an inner class of this class (not even
	 * through multiple enclosures), the return value of this method is
	 * unspecified.
	 * 
	 * @param <T>
	 *            the type for which an instance is required
	 * @param forWhat
	 *            the inner class for which an instance is required
	 * @return <code>null</code> if <code>forWhat</code> is <code>null</code> or
	 *         if creating an instance of <code>forWhat</code> failed
	 */
	<T> T getInnerClassInstance(Class<T> forWhat) {
		if (forWhat == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T ret = (T) innerClassInstances.get(forWhat);
		if (ret == null) {
			Object parent;
			Class<?> enclosing = forWhat.getEnclosingClass();
			if (enclosing.isAssignableFrom(this.getClass())) {
				parent = this;
			} else {
				parent = getInnerClassInstance(enclosing);
			}
			ret = InstanceCreator
					.create(forWhat, parent);
			innerClassInstances.put(forWhat, ret);
		}
		return ret;
	}

}
