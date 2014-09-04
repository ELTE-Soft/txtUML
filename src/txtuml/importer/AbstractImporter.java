package txtuml.importer;

import java.lang.reflect.*;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

import txtuml.api.*;


public abstract class AbstractImporter {
	

	protected static boolean isModelElement(Class<?> c)
	{
		 
		return ModelElement.class.isAssignableFrom(c) ||
			   isState(c) ||
			   isTransition(c) ;
	}
	protected static boolean isClass(Class<?> c) {
		return ModelClass.class.isAssignableFrom(c);
	}

	protected static boolean isAssociation(Class<?> c) {
		return Association.class.isAssignableFrom(c);
	}
    
	protected static boolean isEvent(Class<?> c) {
		return Signal.class.isAssignableFrom(c);
	}

	protected static boolean isAttribute(Field f) {
        return ModelType.class.isAssignableFrom(f.getType());   
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
		for(Method m:containingClass.getDeclaredMethods())
		{
			if(m.getName().equals(name))
			{
				return m;
			}
		}
		return null;
	}

	protected static boolean localInstanceToBeCreated = false;

}
