package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;

public class MultiplicityProvider {
	
	private static Integer getExplicitMultiplicity(ITypeBinding typeDeclaration, String annotationName) {
		for (IAnnotationBinding annot : typeDeclaration.getAnnotations()) {
			if (annot.getName().equals(annotationName)) {
				for (IMemberValuePairBinding pair : annot.getAllMemberValuePairs()) {
					if (pair.getName().equals("value")) {
						return (Integer) pair.getValue();
					}
				}
			}
		}
		return null;
	}

	private static Integer getExplicitLowerBound(ITypeBinding typeDeclaration) {
		return getExplicitMultiplicity(typeDeclaration, Min.class.getSimpleName());
	}

	private static Integer getExplicitUpperBound(ITypeBinding typeDeclaration) {
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
	public static int getLowerBound(ITypeBinding typeDeclaration) {
		Integer explicitLowerBound = getExplicitLowerBound(typeDeclaration);
		if (explicitLowerBound == null) {
			return 0;
		}
		return explicitLowerBound;
	}

	/**
	 * Gets the upper bound of the multiplicity of the txtUML element
	 * represented by the specified class.
	 * 
	 * @param specifiedClass
	 *            The specified class representing a txtUML element.
	 * @return The upper bound of the specified element's multiplicity.
	 */
	public static int getUpperBound(ITypeBinding typeDeclaration) {
		Integer explicitUpperBound = getExplicitUpperBound(typeDeclaration);
		if (explicitUpperBound == null) {
			return org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		}
		return explicitUpperBound;
	}
}
