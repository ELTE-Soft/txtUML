package hu.elte.txtuml.export.uml2.transform.backend;

import java.util.IdentityHashMap;

/**
 * Implementation of the InstancesMap interface.
 * An IdentityHashMap for storing instance information for dummy instances
 * during model import.
 * @author Ádám Ancsin
 *
 */
@SuppressWarnings("serial")
class InstancesMapImpl 
extends IdentityHashMap<Object, InstanceInformation> 
implements InstancesMap
{
	InstancesMapImpl()
	{
		super();
	}
	
	@Override
	public String toString()
	{
		StringBuilder strBuilder = new StringBuilder();
		for(Object obj : this.keySet())
		{
			strBuilder.append("<");
			strBuilder.append(System.identityHashCode(obj));
			strBuilder.append(",");
			strBuilder.append(obj.getClass());
			strBuilder.append(",");
			strBuilder.append(this.get(obj).getExpression());
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}
}
