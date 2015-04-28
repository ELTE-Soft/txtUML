package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * This class provides utilities for assigning modifiers (e.g. static, setting visibility, etc.) to imported model elements.
 * @author Adam Ancsin
 *
 */
public final class ElementModifiersAssigner {

	/**
	 * Sets the visibility of the specified named element based on the modifiers given by Java Reflection.
	 * @param element The specified named element.
	 * @param modifiers The modifiers given by Java Reflection.
	 *
	 * @author Adam Ancsin
	 */
    private static void setVisibilityBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		if(Modifier.isPrivate(modifiers))
			element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		else if(Modifier.isProtected(modifiers))
			element.setVisibility(VisibilityKind.PROTECTED_LITERAL);
		else if(Modifier.isPublic(modifiers))
			element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		else
		{
			if(element instanceof Property)
				element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
			else if(element instanceof Operation || element instanceof org.eclipse.uml2.uml.Classifier)
				element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
			else
				element.setVisibility(VisibilityKind.PACKAGE_LITERAL);
		}
	}
    
	/**
	 * Sets the modifiers of the specified named element based on the modifiers given by Java Reflection.
	 * @param element The specified named element.
	 * @param modifiers The modifiers given by Java Reflection.
	 *
	 * @author Adam Ancsin
	 */
	private static void setElementModifiersBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		setVisibilityBasedOnModifiersGivenByReflection(element,modifiers);
		
		boolean isAbstract = Modifier.isAbstract(modifiers);
		boolean isStatic = Modifier.isStatic(modifiers);
		
		if(element instanceof Classifier)
		{
			Classifier classifierElem=(Classifier) element;
			classifierElem.setIsAbstract(isAbstract);
		}
		if(element instanceof BehavioralFeature)
		{
			BehavioralFeature featureElem = (BehavioralFeature) element;
			featureElem.setIsStatic(isStatic);
			featureElem.setIsAbstract(isAbstract);
		}
		
	}
	
	/**
	 * Sets the modifiers of the specified imported element which is represented by a class in the txtUML model.
	 * @param importedElement The specified imported element.
	 * @param sourceClass The class representing the specified imported element in the txtUML model.
	 *
	 * @author Adam Ancsin
	 */
	public static void setModifiers(NamedElement importedElement,Class<?> sourceClass)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceClass.getModifiers());	
	}
	
	/**
	 * Sets the modifiers of the specified imported element which is represented by a method in the txtUML model.
	 * @param importedElement The specified imported element.
	 * @param sourceMethod The method representing the specified imported element in the txtUML model.
	 *
	 * @author Adam Ancsin
	 */
	public static void setModifiers(NamedElement importedElement, Method sourceMethod)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceMethod.getModifiers());	
	}
	
	/**
	 * Sets the modifiers of the specified imported element which is represented by a field in the txtUML model.
	 * @param importedElement The specified imported element.
	 * @param sourceField The field representing the specified imported element in the txtUML model.
	 *
	 * @author Adam Ancsin
	 */
	public static void setModifiers(NamedElement importedElement,Field sourceField)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceField.getModifiers());	
	}
}
