package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.NestedClassInstancesMap;
import hu.elte.txtuml.utils.InstanceCreator;

abstract class NestedClassInstancesHolder {

	/**
	 * The map which contains instances of the nested classes.
	 */
	private final NestedClassInstancesMap nestedClassInstances = NestedClassInstancesMap
			.create();

	/**
	 * Sole constructor of <code>NestedClassInstancesHolder</code>. Puts itself
	 * inside the map which contains instances of the nested classes.
	 */
	NestedClassInstancesHolder() {
		super();

		this.nestedClassInstances.put(getClass(), this);
	}

	/**
	 * Returns an instance of the specified nested class.
	 * <p>
	 * If the specified class is not a nested class of this class (not even
	 * through multiple enclosures), the return value of this method is
	 * unspecified.
	 * 
	 * @param forWhat
	 *            the nested class for which an instance is required
	 * @return <code>null</code> if <code>forWhat</code> is <code>null</code> or
	 *         if creating an instance of <code>forWhat</code> failed
	 */
	<T> T getNestedClassInstance(Class<T> forWhat) {
		if (forWhat == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T ret = (T) nestedClassInstances.get(forWhat);
		if (ret == null) {
			ret = InstanceCreator.createInstanceWithGivenParams(forWhat,
					getNestedClassInstance(forWhat.getEnclosingClass()));
			nestedClassInstances.put(forWhat, ret);
		}
		return ret;
	}

}
