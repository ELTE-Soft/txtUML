package hu.elte.txtuml.validation.visitors;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.general.InvalidModifier;
import hu.elte.txtuml.validation.problems.general.InvalidTemplate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

public class Utils {

	public static void checkTemplate(ProblemCollector collector, TypeDeclaration elem) {
		if (elem.typeParameters().size() > 0) {
			collector.setProblemStatus(true,
					new InvalidTemplate(collector.getSourceInfo(), (TypeParameter) (elem.typeParameters().get(0))));
		}
	}

	public static void checkModifiers(ProblemCollector collector, BodyDeclaration elem) {
		for (Object obj : elem.modifiers()) {
			IExtendedModifier extModifier = (IExtendedModifier) obj;
			if (extModifier.isModifier()) {
				Modifier modifier = (Modifier) extModifier;
				boolean valid = true;
				if (modifier.isStatic()) {
					// 'static' is only valid for signals
					if (elem instanceof TypeDeclaration) {
						TypeDeclaration typeDecl = (TypeDeclaration) elem;
						if (!ElementTypeTeller.isSignal(typeDecl)) {
							valid = false;
						}
					}
				} else if (modifier.isPrivate() || modifier.isPublic()) {
					// 'private' and 'public' are allowed
				} else {
					valid = false;
				}
				collector.setProblemStatus(!valid, new InvalidModifier(collector.getSourceInfo(), modifier));
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
			SimpleType stype = (SimpleType) type;
			return (stype.getName().toString().equals("String"));
		}

		return false;
	}

	public static boolean isAllowedBasicTypeOrModelClass(Type type, boolean isVoidAllowed) {
		if (isAllowedBasicType(type, isVoidAllowed)) {
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
