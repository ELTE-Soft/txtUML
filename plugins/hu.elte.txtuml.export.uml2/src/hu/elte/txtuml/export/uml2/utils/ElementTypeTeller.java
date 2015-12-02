package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.Composition.Container;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Choice;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Initial;
import hu.elte.txtuml.api.model.StateMachine.State;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.external.ExternalClass;

/**
 * This class provides utilities for telling the types of txtUML model elements.
 * 
 * @author Adam Ancsin
 *
 */
public final class ElementTypeTeller {

	private ElementTypeTeller() {
	}

	public static boolean isVertex(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Vertex.class);
	}

	public static boolean isInitialPseudoState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Initial.class);
	}

	public static boolean isChoicePseudoState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Choice.class);
	}

	public static boolean isState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, State.class);
	}

	public static boolean isCompositeState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				CompositeState.class);
	}

	public static boolean isSimpleState(TypeDeclaration typeDeclaration) {
		return isState(typeDeclaration) && !isCompositeState(typeDeclaration);
	}

	public static boolean isTransition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				Transition.class);
	}

	public static boolean isModelClass(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				ModelClass.class);
	}

	public static boolean isSignal(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Signal.class);
	}

	public static boolean isAssociation(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				Association.class);
	}
	
	public static boolean isComposition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				Composition.class);
	}
	
	public static boolean isContainer(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				Container.class);
	}

	public static boolean isSpecificClassifier(
			TypeDeclaration classifierDeclaration) {
		ITypeBinding superclassBinding = classifierDeclaration.resolveBinding()
				.getSuperclass();
		String superclassQualifiedName = superclassBinding.getQualifiedName();
		boolean extendsModelClass = superclassQualifiedName
				.equals(ModelClass.class.getCanonicalName());
		boolean extendsSignal = superclassQualifiedName.equals(Signal.class
				.getCanonicalName());

		return !extendsModelClass && !extendsSignal;
	}

	public static boolean isAbstract(TypeDeclaration typeDeclaration) {
		for (Object elem : typeDeclaration.modifiers()) {
			IExtendedModifier extendedModifier = (IExtendedModifier) elem;
			if (extendedModifier.isModifier()) {
				Modifier modifier = (Modifier) extendedModifier;
				if (modifier.isAbstract()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isExternalClass(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration,
				ExternalClass.class);
	}
}
