package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.Association;
import hu.elte.txtuml.api.Event;
import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelIdentifiedElement;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine;

public class ElementTypeTeller {

	public static boolean isModelElement(Class<?> c)
	{
		 
		return ModelElement.class.isAssignableFrom(c) ||
			   isState(c) ||
			   isTransition(c) ;
	}
	public static boolean isModelClass(Class<?> c) {
		return ModelClass.class.isAssignableFrom(c);
	}

	public static boolean isExternalClass(Class<?> c) {
		return ExternalClass.class.isAssignableFrom(c);
	}
	public static boolean isClass(Class<?> c )
	{
		return isModelClass(c) || isExternalClass(c);
	}
	public static boolean isEvent(Class<?> c) {
		return Signal.class.isAssignableFrom(c);
	}

	public static boolean isClassifier(Class<?> c)
	{
		return isClass(c) || isEvent(c);
	}
	
	public static boolean isAssociation(Class<?> c) {
		return Association.class.isAssignableFrom(c);
	}
    
	
	public static boolean isAttribute(Field f) {
		return ModelIdentifiedElement.class.isAssignableFrom(f.getType());
    }
    
	public static boolean isState(Class<?> c) {
        return StateMachine.State.class.isAssignableFrom(c);
    }

	public static boolean isInitialState(Class<?> c) {
        return StateMachine.InitialState.class.isAssignableFrom(c);
    }

	public static boolean isCompositeState(Class<?> c) {
        return StateMachine.CompositeState.class.isAssignableFrom(c);
    }
	
	public static boolean isChoice(Class<?> c)
	{
		return StateMachine.Choice.class.isAssignableFrom(c);
	}

	public static boolean isTransition(Class<?> c) {
        return StateMachine.Transition.class.isAssignableFrom(c);
    }
	
	public static boolean isModelClass(ModelElement element) {
		return element!=null && isModelClass(element.getClass());
	}

	public static boolean isExternalClass(ModelElement element) {
		return element!=null && isExternalClass(element.getClass());
	}
	public static boolean isClass(ModelElement  element )
	{
		return isModelClass(element) || isExternalClass(element);
	}
	public static boolean isEvent(ModelElement element) {
		return element!=null && isEvent(element.getClass());
	}

	public static boolean isClassifier(ModelElement element)
	{
		return isClass(element) || isEvent(element);
	}
	
	public static boolean isAssociation(ModelElement element) {
		return element!=null && isAssociation(element.getClass());
	}
        
	public static boolean isState(ModelElement element) {
		return element!=null && isState(element.getClass());
    }

	public static boolean isInitialState(ModelElement element) {
		return element!=null && isInitialState(element.getClass());
    }

	public static boolean isCompositeState(ModelElement element) {
		return element!=null && isCompositeState(element.getClass());
    }
	
	public static boolean isChoice(ModelElement element) {
		return element!=null && isChoice(element.getClass());
    }

	public static boolean isTransition(ModelElement element) {
		return element!=null && isTransition(element.getClass());
    }
	public static boolean isSpecificClassifier(Class<?> c)
	{
		if(!isClassifier(c))
			return false;
		
		Class<?> superClass=c.getSuperclass();
		
		if(superClass==null)
			return false;
		else if(superClass==ModelClass.class)
			return false;
		else if(superClass==ExternalClass.class)
			return false;
		else if(superClass==Signal.class)
			return false;
		else if(superClass==Event.class)
			return false;
		else
			return true;
		
	}
    
	
}
