package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.InnerClassInstancesMap;
import hu.elte.txtuml.utils.InstanceCreator;

abstract class InnerClassInstancesHolder {

	private final InnerClassInstancesMap innerClassInstances = InnerClassInstancesMap
			.create();
	
	InnerClassInstancesHolder() {
		super();
		
		this.innerClassInstances.put(getClass(), this);
	}
	
	<T> T getInnerClassInstance(Class<T> forWhat) {
		if (forWhat == null) {
			// TODO show error
			return null;
		}
		@SuppressWarnings("unchecked")
		T ret = (T) innerClassInstances.get(forWhat);
		if (ret == null) {
			ret = InstanceCreator.createInstanceWithGivenParams(forWhat,
					getInnerClassInstance(forWhat.getEnclosingClass()));
			innerClassInstances.put(forWhat, ret);
		}
		return ret;
	}
	
}
