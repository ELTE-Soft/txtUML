package hu.elte.txtuml.export.uml2.utils;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;

/**
 * This class provides utilities for finding elements.
 * @author Adam Ancsin
 *
 */
public final class ElementFinder {

	/**
	 * Searches for a field with the given field name in a specified class.
	 * @param fieldName The given field name.
	 * @param type The specified class.
	 * @return The field. (null, if not found)
	 *
	 * @author Adam Ancsin
	 */
	public static Field findField(String fieldName,Class<?> type) {

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
	/**
	 * Searches for a method with the given method name in a specified class.
	 *
	 * @param name The given method name.
	 * @param containingClass The specified class.
	 * @return The method. (null, if not found)
	 *
	 * @author Adam Ancsin
	 */
	public static Method findMethod(String name, Class<?> containingClass)
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

	/**
	 * Searches for an UML2 Operation with the given name in the specified UML2 class.
	 *
	 * @param name The given UML2 Operation name.
	 * @param ownerClass The specified UML2 class.
	 * @return The UML2 Operation (null, if not found)
	 *
	 * @author Adam Ancsin
	 */
	public static Operation findOperation(String name, org.eclipse.uml2.uml.Class ownerClass)
	{
		for(Operation op:ownerClass.getAllOperations())
		{
			if(op.getName().equals(name))
			{
				return op;
			}
		}
		return null;
	}
	
	/**
	 * Searches for a UML2 Parameter with the given parameter name in the specified UML2 Activity.
	 * @param paramName The given parameter name.
	 * @param activity The specified UML2 Activity.
	 * @return The UML2 parameter (null, if not found).
	 *
	 * @author Adam Ancsin
	 */
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
	
	/**
	 * Searches for a declared class with the given name in the specified enclosing class.
	 *
	 * @param classToFindName The given name of the class to find.
	 * @param enclosingClass The specified enclosing class.
	 * @return The declared class (null, if not found).
	 *
	 * @author Adam Ancsin
	 */
	public static Class<?> findDeclaredClass(String classToFindName, Class<?> enclosingClass)
	{
		for(Class<?> c : enclosingClass.getDeclaredClasses())
		{
			String className=c.getName();
			if(className.equals(classToFindName))
			{
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Searches for an UML2 association member end with the given end name of the specified UML2 association.
	 *
	 * @param endToFindName The given UML2 association member end name.
	 * @param association The specified UML2 association.
	 * @return The UML2 association member end (null if not found).
	 *
	 * @author Adam Ancsin
	 */
	public static Property findAssociationMemberEnd(String endToFindName, Association association)
	{
		for(Property memberEnd:association.getMemberEnds())
		{
			String memberEndName=memberEnd.getName();
			if(memberEndName.equals(endToFindName))
			{
				return memberEnd;
			}
		}
		return null;
	}

	/**
	 * Searches for the class of a txtUML model with the specified class name.
	 * @param className The specified class name-
	 * @return The class of the txtUML model (null, if not found).
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	public static Class<?> findModel(String className) throws ImportException 
	{
		try 
		{
			Class<?> ret = Class.forName(className);
			
			if(!Model.class.isAssignableFrom(ret))
				throw new ImportException("A subclass of Model is expected, got: " + className);
			
			return ret;
		}
		catch(ClassNotFoundException e) 
		{
			throw new ImportException("Cannot find class: " + className);
		}
	}
}
