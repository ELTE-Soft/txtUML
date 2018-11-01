package hu.elte.txtuml.layout.export.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.interfaces.ParentMap;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;

/**
 * Default implementation for {@link NodeMap}.
 */
@SuppressWarnings("serial")
public class NodeMapImpl extends LinkedHashMap<Class<?>, NodeInfo> implements NodeMap {
	
	private ParentMap _parentMap;
	
	public NodeMapImpl() {
		_parentMap = ParentMap.create();
	}
	
	@Override
	public NodeInfo put(Class<?> key, NodeInfo value) {
		
		if(_parentMap.isInParent())
		{
			_parentMap.put(key);
		}
		
		return super.put(key, value);
	}
	
	@Override
	public void startOfParent(Class<?> node)
	{
		_parentMap.addNew(node);
	}
	
	@Override
	public void setParent(Class<?> child, Class<?> parent)
	{
		if(!_parentMap.containsKey(child))
			_parentMap.put(child,  parent);
	}
	
	@Override
	public Class<?> getCurrentParent() {
		return _parentMap.getLast();
	}
	
	@Override
	public Class<?> getParent(Class<?> child) {
		return _parentMap.get(child);
	}
	
	@Override
	public void endOfParent()
	{
		_parentMap.removeLast();
	}
	
	private Set<RectangleObject> converted;
	
	@Override
	public Set<RectangleObject> convert(DiagramType dType) {
		converted = new HashSet<>();
		
		for(Class<?> node : this.keySet())
		{
			convertNode(node, dType);
		}

		return converted;
	}

	private RectangleObject convertNode(Class<?> nodeToConvert, DiagramType dType)
	{
		RectangleObject convertedNode = this.get(nodeToConvert).convert();
		
		RectangleObject containedBox = containsRecursively(converted, convertedNode);
		if(containedBox != null)
			return containedBox;
		
		if(_parentMap.containsKey(nodeToConvert))
		{
			RectangleObject parent = convertNode(_parentMap.get(nodeToConvert), dType);
			if(!parent.hasInner())
			{
				parent.setInner(new Diagram(dType.convert()));
			}
			parent.getInner().Objects.add(convertedNode);
		}
		else
			converted.add(convertedNode);
		
		return convertedNode;
	}
	
	private RectangleObject containsRecursively(Set<RectangleObject> resultSet, 
			RectangleObject toSearch) {
		for(RectangleObject box : new RectangleObjectTreeEnumerator(resultSet)) {
			if(box.equals(toSearch))
				return box;
		}
		
		return null;
	}

}
