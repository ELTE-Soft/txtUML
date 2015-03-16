package hu.elte.txtuml.export.uml2.transform.backend;

import java.util.IdentityHashMap;

@SuppressWarnings("serial")
class InstancesMapImpl 
extends IdentityHashMap<Object, InstanceInformation> 
implements InstancesMap{

	InstancesMapImpl()
	{
		super();
	}
	
	@Override
	public String toString()
	{
		String ret="";
		for(Object obj : this.keySet())
		{
			ret+="<"+System.identityHashCode(obj)+","+obj.getClass()+","+this.get(obj).getExpression()+">\n";
		}
		return ret;
	}

}
