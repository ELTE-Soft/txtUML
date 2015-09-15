package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Event;
import hu.elte.txtuml.api.model.ExternalClass;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelElement;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine;


/**
 * This class provides utilities for telling the types of txtUML model elements.
 * @author Adam Ancsin
 *
 */
public final class ElementTypeTeller {

	/**
	 * Decides if the specified class represents a txtUML model element.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isModelElement(Class<?> specifiedClass)
	{ 
		return ModelElement.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the specified class represents a txtUML model class.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isModelClass(Class<?> specifiedClass) 
	{
		return ModelClass.class.isAssignableFrom(specifiedClass);
	}

	/**
	 * Decides if the specified class represents a txtUML external class.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isExternalClass(Class<?> specifiedClass)
	{
		return ExternalClass.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the specified class represents a txtUML class (model class or external class).
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isClass(Class<?> specifiedClass)
	{
		return isModelClass(specifiedClass) || isExternalClass(specifiedClass);
	}
	
	/**
	 * Decides if the specified class represents a txtUML event.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isEvent(Class<?> specifiedClass) 
	{
		return Signal.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the specified class represents a txtUML classifier (class or event).
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isClassifier(Class<?> specifiedClass)
	{
		return isClass(specifiedClass) || isEvent(specifiedClass);
	}
	
	/**
	 * Decides if the specified class represents a txtUML association.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isAssociation(Class<?> specifiedClass)
	{
		return Association.class.isAssignableFrom(specifiedClass);
	}
    
	/**
	 * Decides if the specified field represents a txtUML classifier attribute.
	 * @param field The specified field.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isAttribute(Field field)
	{
		return 	ModelElement.class.isAssignableFrom(field.getType()) || 
				ExternalClass.class.isAssignableFrom(field.getType());
    }
    
	/**
	 * Decides if the specified class represents a txtUML state machine vertex.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isVertex(Class<?> specifiedClass)
	{
        return StateMachine.Vertex.class.isAssignableFrom(specifiedClass);
    }	
	
	/**
	 * Decides if the specified class represents a txtUML state.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isState(Class<?> specifiedClass)
	{
        return StateMachine.State.class.isAssignableFrom(specifiedClass);
    }

	/**
	 * Decides if the specified class represents a txtUML initial pseudostate.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isInitial(Class<?> specifiedClass)
	{
        return StateMachine.Initial.class.isAssignableFrom(specifiedClass);
    }

	/**
	 * Decides if the specified class represents a txtUML composite state.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isCompositeState(Class<?> specifiedClass)
	{
        return StateMachine.CompositeState.class.isAssignableFrom(specifiedClass);
    }
	
	/**
	 * Decides if the specified class represents a txtUML choice pseudostate.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isChoice(Class<?> specifiedClass)
	{
		return StateMachine.Choice.class.isAssignableFrom(specifiedClass);
	}

	/**
	 * Decides if the specified class represents a txtUML state machine transition.
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isTransition(Class<?> specifiedClass)
	{
        return StateMachine.Transition.class.isAssignableFrom(specifiedClass);
    }
	
	/**
	 * Decides if the specified model element is a txtUML model class instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isModelClass(ModelElement element)
	{
		return element!=null && isModelClass(element.getClass());
	}

	/**
	 * Decides if the specified model element is a txtUML external class instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isExternalClass(ModelElement element)
	{
		return element!=null && isExternalClass(element.getClass());
	}
	
	/**
	 * Decides if the specified model element is a txtUML class (model/external class) instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isClass(ModelElement element)
	{
		return isModelClass(element) || isExternalClass(element);
	}
	
	/**
	 * Decides if the specified model element is a txtUML event instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isEvent(ModelElement element)
	{
		return element!=null && isEvent(element.getClass());
	}

	/**
	 * Decides if the specified model element is a txtUML classifier(class/event) instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isClassifier(ModelElement element)
	{
		return isClass(element) || isEvent(element);
	}
	
	/**
	 * Decides if the specified model element is a txtUML association instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isAssociation(ModelElement element) 
	{
		return element!=null && isAssociation(element.getClass());
	}

	/**
	 * Decides if the specified model element is a txtUML state machine vertex instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isVertex(ModelElement element) 
	{
		return element!=null && isVertex(element.getClass());
    }
	
	/**
	 * Decides if the specified model element is a txtUML state instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isState(ModelElement element)
	{
		return element!=null && isState(element.getClass());
    }

	/**
	 * Decides if the specified model element is a txtUML initial pseudostate instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isInitial(ModelElement element)
	{
		return element!=null && isInitial(element.getClass());
    }

	/**
	 * Decides if the specified model element is a txtUML composite state instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isCompositeState(ModelElement element) 
	{
		return element!=null && isCompositeState(element.getClass());
    }
	
	/**
	 * Decides if the specified model element is a txtUML choice pseudostate instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isChoice(ModelElement element)
	{
		return element!=null && isChoice(element.getClass());
    }

	/**
	 * Decides if the specified model element is a txtUML transition instance.
	 * @param element The specified model element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isTransition(ModelElement element)
	{
		return element!=null && isTransition(element.getClass());
    }
	
	/**
	 * Decides if the specified class represents a specific txtUML classifier (class/event).
	 * @param specifiedClass The specified class.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isSpecificClassifier(Class<?> specifiedClass)
	{
		if(!isClassifier(specifiedClass))
			return false;
		
		Class<?> superClass=specifiedClass.getSuperclass();
		
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
