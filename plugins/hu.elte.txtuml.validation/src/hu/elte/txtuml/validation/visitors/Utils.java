package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.general.InvalidModifier;
import hu.elte.txtuml.validation.problems.general.InvalidTemplate;

/**
 * Utility methods for visitors to check common AST elements.
 */
public class Utils {

	public static void checkTemplate(ProblemCollector collector, TypeDeclaration elem) {
		if (elem.typeParameters().size() > 0) {
			collector.setProblemStatus(
					new InvalidTemplate(collector.getSourceInfo(), (TypeParameter) (elem.typeParameters().get(0))));
		}
	}

	public static void checkModifiers(ProblemCollector collector, BodyDeclaration elem) {
		for (Object obj : elem.modifiers()) {
			if (!(obj instanceof Modifier)) {
				continue;
			}
			Modifier modifier = (Modifier) obj;
			boolean valid;
			if (modifier.isStatic()) {
				valid = elem instanceof TypeDeclaration && ElementTypeTeller.isSignal((TypeDeclaration) elem);
			} else {
				valid = modifier.isPrivate() || modifier.isPublic();
			}
			if (!valid) {
				collector.setProblemStatus(new InvalidModifier(collector.getSourceInfo(), modifier));
			}
		}
	}

	public static boolean isAllowedBasicType(Type type, boolean isVoidAllowed) {
		if (type.isPrimitiveType()) {
			PrimitiveType.Code code = ((PrimitiveType) type).getPrimitiveTypeCode();
			return (code == PrimitiveType.BOOLEAN || code == PrimitiveType.DOUBLE || code == PrimitiveType.INT
					|| (code == PrimitiveType.VOID && isVoidAllowed));
		}

		if (type.isSimpleType()) {
			type.resolveBinding().getQualifiedName().equals("java.lang.String"); //$NON-NLS-1$
		}

		return false;
	}

	public static boolean isAllowedBasicTypeOrModelClass(Type type, boolean isVoidAllowed) {
		if (isAllowedBasicType(type, isVoidAllowed) || ElementTypeTeller.isModelClass(type.resolveBinding())) {
			return true;
		}

		return (SharedUtils.typeIsAssignableFrom(type.resolveBinding(), ModelClass.class));
	}

	public static boolean isVoid(Type type) {
		if (type instanceof PrimitiveType) {
			return ((PrimitiveType) type).getPrimitiveTypeCode().equals(PrimitiveType.VOID);
		} else {
			return false;
		}
	}

	public static boolean isBoolean(Type type) {
		if (type instanceof PrimitiveType) {
			return ((PrimitiveType) type).getPrimitiveTypeCode().equals(PrimitiveType.BOOLEAN);
		} else {
			return false;
		}
	}

}
