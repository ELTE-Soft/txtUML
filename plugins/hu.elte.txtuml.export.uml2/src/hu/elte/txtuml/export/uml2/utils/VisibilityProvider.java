package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Modifier;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;

public final class VisibilityProvider {
	private VisibilityProvider() {
	}

	public static VisibilityKind getVisibilityOfNamedElementFromModifiers(
			NamedElement element, int modifiers) {
		if (Modifier.isPrivate(modifiers)) {
			return VisibilityKind.PRIVATE_LITERAL;
		} else if (Modifier.isProtected(modifiers)) {
			return VisibilityKind.PROTECTED_LITERAL;
		} else if (Modifier.isPublic(modifiers)) {
			return VisibilityKind.PUBLIC_LITERAL;
		} else {
			return getVisibilityOfElementWithoutVisibilityModifier(element);
		}
	}

	private static VisibilityKind getVisibilityOfElementWithoutVisibilityModifier(
			NamedElement element) {
		if (element instanceof Property) {
			return VisibilityKind.PRIVATE_LITERAL;
		} else if (element instanceof Operation
				|| element instanceof org.eclipse.uml2.uml.Classifier) {
			return VisibilityKind.PUBLIC_LITERAL;
		} else {
			return VisibilityKind.PACKAGE_LITERAL;
		}
	}
}
