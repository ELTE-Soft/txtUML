package hu.elte.txtuml.layout.export.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.State;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Default implementation for {@link NodeMap}.
 */
@SuppressWarnings("serial")
public class NodeMapImpl extends LinkedHashMap<Class<?>, NodeInfo> implements NodeMap {
	
	@Override
	public Set<RectangleObject> convert() {
		Set<RectangleObject> set = new HashSet<>();
		
		for(Entry<Class<?>, NodeInfo> entry : this.entrySet()){
			RectangleObject converted = entry.getValue().convert();
			if(!set.contains(converted))
			{
				if(CompositeState.class.isAssignableFrom(entry.getKey()))
			    {
			    	converted.setInner(new Diagram(convertComposite(entry.getKey()), null));
			    }
				
				set.add(converted);
			}
		};

		return set;
	}
	
	private Set<RectangleObject> convertComposite(Class<?> comp)
	{
		Set<RectangleObject> result = new HashSet<RectangleObject>();
		
		Class<?>[] children = comp.getDeclaredClasses();
    	for(Class<?> cls : children)
    	{
    		if(CompositeState.class.isAssignableFrom(cls))
		    {
    			result.addAll(convertComposite(cls));
		    }
    		else if(State.class.isAssignableFrom(cls))
    		{
    			NodeInfo v = this.get(cls);
    			if(v != null && !result.contains(v))
    				result.add(v.convert());
    		}
    	}
    	
    	return result;
	}

}
