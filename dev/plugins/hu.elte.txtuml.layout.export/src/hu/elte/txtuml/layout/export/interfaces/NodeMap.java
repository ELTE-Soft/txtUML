package hu.elte.txtuml.layout.export.interfaces;

import java.util.Map;
import java.util.Set;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.impl.NodeMapImpl;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public interface NodeMap extends Map<Class<?>, NodeInfo> {

	static NodeMap create() {
		return new NodeMapImpl();
	}
	
	Set<RectangleObject> convert(DiagramType dType);
	
	void startOfParent(Class<?> node);
	void setParent(Class<?> child, Class<?> parent);
	Class<?> getCurrentParent();
	Class<?> getParent(Class<?> child);
	void endOfParent();
	
}
