package txtuml.importer.utils;

import java.lang.reflect.Field;

import txtuml.api.Association;
import txtuml.api.ExternalClass;
import txtuml.api.ModelClass;
import txtuml.api.ModelElement;
import txtuml.api.ModelIdentifiedElement;
import txtuml.api.Signal;
import txtuml.api.StateMachine;

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
    
	
}
