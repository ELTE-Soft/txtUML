package txtuml.importer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;

public class ElementFinder {

	public static Field findField(Class<?> type,String fieldName) {

	    Class<?> i = type;
	    while (i != null && i != Object.class) {
	        for(Field f:i.getDeclaredFields())
	        {
	        	if(fieldName.equals(f.getName()))
	        	{
	        		return f;
	        	}
	        }
	        i = i.getSuperclass();
	    }

	    return null;
	}
	
	public static Method findMethod(Class<?> containingClass, String name)
	{
		for(Method m: containingClass.getDeclaredMethods())
		{
			if(m.getName().equals(name))
			{
				return m;
			}
		}
		
		for(Method m: containingClass.getMethods())
		{
			if(m.getName().equals(name))
			{
				return m;
			}
		}
		return null;
	}

	public static Operation findOperation(org.eclipse.uml2.uml.Class ownerClass,String name)
	{
		for(Operation op:ownerClass.getOperations())
		{
			if(op.getName().equals(name))
			{
				return op;
			}
		}
		return null;
	}
	
	public static Parameter findParameterInActivity(String paramName, Activity activity)
	{
		for(Parameter p : activity.getSpecification().getOwnedParameters())
		{	
			if(p.getName().equals(paramName))
			{
				return p;
			}
			
		}
		return null;
	}
}
