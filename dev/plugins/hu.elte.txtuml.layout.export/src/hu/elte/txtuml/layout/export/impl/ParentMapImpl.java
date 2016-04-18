package hu.elte.txtuml.layout.export.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import hu.elte.txtuml.layout.export.interfaces.ParentMap;

/**
 * Default implementation for {@link ParentMap}.
 */
@SuppressWarnings("serial")
public class ParentMapImpl extends LinkedHashMap<Class<?>, Class<?>> 
implements ParentMap {

	private LinkedList<Class<?>> _parentTrace;
	
	public ParentMapImpl()
	{
		_parentTrace = new LinkedList<Class<?>>();
	}
	
	@Override
	public boolean isInParent()
	{
		return _parentTrace.size() > 0;
	}
	
	@Override
	public Class<?> put(Class<?> subClass)
	{
		return super.put(subClass, _parentTrace.getLast());
	}
	
	@Override
	public void addNew(Class<?> parent)
	{
		_parentTrace.addLast(parent);
	}
	
	@Override
	public Class<?> removeLast()
	{
		return _parentTrace.removeLast();
	}
}
