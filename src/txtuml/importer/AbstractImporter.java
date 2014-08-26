package txtuml.importer;

import java.lang.reflect.*;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

import txtuml.api.*;


public abstract class AbstractImporter {
	
	protected static Class<?> findModel(String className) throws ImportException {
		try {
			Class<?> ret = Class.forName(className);
			if(!Model.class.isAssignableFrom(ret)) {
				//throw new ImportException("A subclass of Model is expected, got: " + className);
			}
			return ret;
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
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
		 return m.getParameterTypes().length == 0; // TODO remove when parameters are handled
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
	
	protected static boolean hasInitialState(Region region)
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
	
	protected static boolean localInstanceToBeCreated = false;
	
	
	
}
