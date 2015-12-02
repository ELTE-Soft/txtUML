package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.Container;
import hu.elte.txtuml.api.model.Model;
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

	public static boolean isModelElement(CompilationUnit unit) {
		try {
			IJavaElement javaElement = unit.getJavaElement();
			IPackageFragment pack;
			if (javaElement instanceof ICompilationUnit) {
				pack = (IPackageFragment) javaElement.getParent();
				for (IJavaElement element : pack.getChildren()) {
					if (element instanceof IAnnotation) {
						if (((IAnnotation) element).getElementName().equals(Model.class.getCanonicalName())) {
							return true;
						}
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO: use PluginLogWrapper
			e.printStackTrace();
		}
		return false;
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
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, CompositeState.class);
	}

	public static boolean isSimpleState(TypeDeclaration typeDeclaration) {
		return isState(typeDeclaration) && !isCompositeState(typeDeclaration);
	}

	public static boolean isTransition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Transition.class);
	}

	public static boolean isSignal(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Signal.class);
	}

	public static boolean isAssociation(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Association.class);
	}

	public static boolean isAssociationeEnd(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, AssociationEnd.class);
	}

	public static boolean isComposition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Composition.class);
	}

	public static boolean isContainer(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Container.class);
	}

	public static boolean isSpecificClassifier(TypeDeclaration classifierDeclaration) {
		ITypeBinding superclassBinding = classifierDeclaration.resolveBinding().getSuperclass();
		String superclassQualifiedName = superclassBinding.getQualifiedName();
		boolean extendsModelClass = superclassQualifiedName.equals(ModelClass.class.getCanonicalName());
		boolean extendsSignal = superclassQualifiedName.equals(Signal.class.getCanonicalName());

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
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, ExternalClass.class);
	}
}
