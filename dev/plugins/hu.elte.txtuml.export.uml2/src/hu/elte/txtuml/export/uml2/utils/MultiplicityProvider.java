package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.assocends.Multiplicity;
import hu.elte.txtuml.utils.jdt.SharedUtils;

public class MultiplicityProvider {

	/**
	 * Decides if the txtUML element represented by the specified class has 1..1
	 * multiplicity.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The decision.
	 */
	public static boolean isOne(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Multiplicity.One.class);
	}

	/**
	 * Decides if the txtUML element represented by the specified class has 0..1
	 * multiplicity.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The decision.
	 */
	public static boolean isZeroToOne(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Multiplicity.ZeroToOne.class);
	}

	/**
	 * Decides if the txtUML element represented by the specified class has 0..*
	 * multiplicity.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The decision.
	 */
	public static boolean isZeroToUnlimited(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Multiplicity.ZeroToUnlimited.class);
	}

	/**
	 * Decides if the txtUML element represented by the specified class has 1..*
	 * multiplicity.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The decision.
	 */
	public static boolean isOneToUnlimited(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Multiplicity.OneToUnlimited.class);
	}
	/**
	 * Decides if the txtUML element represented by the specified class has *..*
	 * multiplicity.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The decision.
	 */
	public static boolean isMultiple(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Multiplicity.MinToMax.class);
	}

	private static Integer getExplicitMultiplicity(TypeDeclaration typeDeclaration, String annotationName) {
		for (Object modifier : typeDeclaration.modifiers()) {
			if (modifier instanceof SingleMemberAnnotation) {
				SingleMemberAnnotation annotation = (SingleMemberAnnotation) modifier;
				if (annotation.getTypeName().toString().equals(annotationName)) {
					Expression value = annotation.getValue();
					if (value instanceof NumberLiteral) {
						NumberLiteral num = (NumberLiteral) value;
						try {
							return new Integer(Integer.parseInt(num.getToken()));
						} catch (NumberFormatException e) {
							// Just let it fall through to returning 'null'
							// below.
						}
					}
				}
			}
		}
		return null;
	}

	public static Integer getExplicitLowerBound(TypeDeclaration typeDeclaration) {
		return getExplicitMultiplicity(typeDeclaration, Min.class.getSimpleName());
	}

	public static Integer getExplicitUpperBound(TypeDeclaration typeDeclaration) {
		return getExplicitMultiplicity(typeDeclaration, Max.class.getSimpleName());
	}

	/**
	 * Gets the lower bound of the multiplicity of the txtUML element
	 * represented by the specified class.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The lower bound of the specified element's multiplicity.
	 */
	public static int getLowerBound(TypeDeclaration typeDeclaration) {
		Integer explicitLowerBound = getExplicitLowerBound(typeDeclaration);
		if (explicitLowerBound != null) {
			return explicitLowerBound;
		}
		if (isZeroToOne(typeDeclaration) || isZeroToUnlimited(typeDeclaration)|| isMultiple(typeDeclaration))
			return 0;
		else
			return 1;
	}

	/**
	 * Gets the upper bound of the multiplicity of the txtUML element
	 * represented by the specified class.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The upper bound of the specified element's multiplicity.
	 */
	public static int getUpperBound(TypeDeclaration typeDeclaration) {
		Integer explicitUpperBound = getExplicitUpperBound(typeDeclaration);
		if (explicitUpperBound != null) {
			return explicitUpperBound;
		}
		if (isOneToUnlimited(typeDeclaration) || isZeroToUnlimited(typeDeclaration)|| isMultiple(typeDeclaration))
			return org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		else
			return 1;
	}
}
