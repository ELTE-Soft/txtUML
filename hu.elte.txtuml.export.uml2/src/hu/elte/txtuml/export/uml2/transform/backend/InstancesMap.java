package hu.elte.txtuml.export.uml2.transform.backend;

import java.util.Map;

public interface InstancesMap extends
		Map<Object, ModelElementInformation> {

	static InstancesMapImpl create()
	{
		return new InstancesMapImpl();
	}
}
