package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

public class ElementModifiersSetter {

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
	public static void setModifiers(NamedElement importedElement,Class<?> sourceClass)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceClass.getModifiers());	
	}
	public static void setModifiers(NamedElement importedElement, Method sourceMethod)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceMethod.getModifiers());	
	}
	public static void setModifiers(NamedElement importedElement,Field sourceField)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceField.getModifiers());	
	}
}
