package txtuml.importer;

import java.lang.reflect.*;
import java.util.WeakHashMap;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.VisibilityKind;

import txtuml.api.*;

abstract class AbstractImporter {
	
	protected static Object getObjectFieldVal(Object object,String fieldName)
	{
		
		Field field = getField(object.getClass(),fieldName);
		field.setAccessible(true);
		Object val=null;
		try {
			val = field.get(object);
			return val;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		field.setAccessible(false);
			
		
		return null;
		
	}
	
	protected static Field getField(Class<?> type,String fieldName) {

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
	protected static boolean isModelElement(Class<?> c)
	{
		 
		return ModelElement.class.isAssignableFrom(c) ||
			   isState(c) ||
			   isTransition(c) ;
	}
	protected static boolean isModelClass(Class<?> c) {
		return ModelClass.class.isAssignableFrom(c);
	}

	protected static boolean isExternalClass(Class<?> c) {
		return ExternalClass.class.isAssignableFrom(c);
	}
	protected static boolean isClass(Class<?> c )
	{
		return isModelClass(c) || isExternalClass(c);
	}
	
	protected static boolean isAssociation(Class<?> c) {
		return Association.class.isAssignableFrom(c);
	}
    
	protected static boolean isEvent(Class<?> c) {
		return Signal.class.isAssignableFrom(c);
	}

	protected static boolean isAttribute(Field f) {
		return ModelIdentifiedElement.class.isAssignableFrom(f.getType());
    }
    
	protected static boolean isState(Class<?> c) {
        return ModelClass.State.class.isAssignableFrom(c);
    }

	protected static boolean isInitialState(Class<?> c) {
        return ModelClass.InitialState.class.isAssignableFrom(c);
    }

	protected static boolean isCompositeState(Class<?> c) {
        return ModelClass.CompositeState.class.isAssignableFrom(c);
    }
	
	protected static boolean isChoice(Class<?> c)
	{
		return ModelClass.Choice.class.isAssignableFrom(c);
	}

	protected static boolean isTransition(Class<?> c) {
        return ModelClass.Transition.class.isAssignableFrom(c);
    }
    
	protected static boolean isMemberFunction(Method m) {
		// return m.getParameterTypes().length == 0; // TODO remove when parameters are handled
		return true;
    }
	
	protected static void importWarning(String msg) {
		System.out.println("Warning: " + msg);
	}
	
	protected static boolean isContainsStateMachine(Class<?> sourceClass){
		for(Class<?> c : sourceClass.getDeclaredClasses()){
			if(isState(c)){
				return true;
			}
	    }
		return false;
    }
	
	protected static boolean isContainsInitialState(Region region)
	{
		for(Object vert: region.getSubvertices().toArray())
		{
			if(vert instanceof Pseudostate)
			{
				if(((Pseudostate) vert).getKind()==PseudostateKind.INITIAL_LITERAL)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	protected static void setLocalInstanceToBeCreated(boolean bool) {
		localInstanceToBeCreated = bool;
	}
	
	protected static Method findMethod(Class<?> containingClass, String name)
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

	private static void setVisibilityBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		if(Modifier.isPrivate(modifiers))
		{
			element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		}
		else if(Modifier.isProtected(modifiers))
		{
			element.setVisibility(VisibilityKind.PROTECTED_LITERAL);
		}
		else if(Modifier.isPublic(modifiers))
		{
			element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		}
		else
		{
			if(element instanceof Property)
			{
				element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
			}
			else if(element instanceof Operation || element instanceof org.eclipse.uml2.uml.Classifier)
			{
				element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
			}
			else
			{
				element.setVisibility(VisibilityKind.PACKAGE_LITERAL);
			}
		}
	}
	private static void setElementModifiersBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		setVisibilityBasedOnModifiersGivenByReflection(element,modifiers);
		
		if(element instanceof Classifier)
		{
			boolean isAbstract = Modifier.isAbstract(modifiers);
			Classifier classifierElem=(Classifier) element;
			classifierElem.setIsAbstract(isAbstract);
		}
		
	}
	protected static void setModifiers(NamedElement importedElement,Class<?> sourceClass)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceClass.getModifiers());	
	}
	protected static void setModifiers(NamedElement importedElement, Method sourceMethod)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceMethod.getModifiers());	
	}
	protected static void setModifiers(NamedElement importedElement,Field sourceField)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceField.getModifiers());	
	}

	protected static boolean localInstanceToBeCreated = false;
	protected static PrimitiveType UML2Integer,UML2Bool,UML2String,UML2Real,UML2UnlimitedNatural;
	protected static Class<?> modelClass=null;
	protected static WeakHashMap<ModelType<?>, ModelTypeInformation> modelTypeInstancesInfo=null;
	protected static Operation findOperation(org.eclipse.uml2.uml.Class ownerClass,String name)
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

}
