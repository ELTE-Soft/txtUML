package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

public final class ElementModifiersAssigner {

	private ElementModifiersAssigner() {
	}
	
	public static void assignModifiersForElementBasedOnDeclaration(NamedElement element, BodyDeclaration declaration) {
		int modifiers = declaration.getModifiers();
		VisibilityKind visibility = VisibilityProvider.getVisibilityOfNamedElementFromModifiers(element, modifiers);
		element.setVisibility(visibility);
		
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
		if(element instanceof Property)
		{
			Property propertyElem = (Property) element;
			propertyElem.setIsStatic(isStatic);
		}
	}
	
}
