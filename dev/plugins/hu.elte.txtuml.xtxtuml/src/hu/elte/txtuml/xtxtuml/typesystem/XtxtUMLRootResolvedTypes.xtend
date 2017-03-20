package hu.elte.txtuml.xtxtuml.typesystem;

import org.eclipse.xtext.diagnostics.AbstractDiagnostic
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.validation.EObjectDiagnosticImpl
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.typesystem.internal.DefaultReentrantTypeResolver
import org.eclipse.xtext.xbase.typesystem.internal.RootResolvedTypes
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference
import org.eclipse.xtext.xbase.validation.IssueCodes

class XtxtUMLRootResolvedTypes extends RootResolvedTypes {

	protected new(DefaultReentrantTypeResolver resolver, CancelIndicator monitor) {
		super(resolver, monitor)
	}

	/**
	 * Overrides the super behavior to use fully qualified names in type mismatch error messages
	 * in case of the simple type names are equal. Copied from the super implementation, the
	 * customized part is marked with comments.
	 */
	override protected AbstractDiagnostic createTypeDiagnostic(XExpression expression,
		LightweightTypeReference actualType, LightweightTypeReference expectedType) {
		if (!expectedType.isAny) {
			val actualName = actualType.simpleName;
			val expectedName = expectedType.simpleName;
			if (actualName == expectedName) {
				if (expectedType.isAssignableFrom(actualType)) {
					return null;
				}
			}

			if (expression.eContainingFeature ==
				XbasePackage.Literals.XABSTRACT_FEATURE_CALL__IMPLICIT_FIRST_ARGUMENT) {
				return new EObjectDiagnosticImpl(Severity.ERROR, IssueCodes.INCOMPATIBLE_TYPES,
					String.format("Type mismatch: cannot convert implicit first argument from %s to %s",
						actualType.humanReadableName, expectedType.humanReadableName), expression, null, -1, null);
			} else {
				// start of customized code

				val typePair = if (actualType.humanReadableName == expectedType.humanReadableName) {
						actualType.identifier + " to " + expectedType.identifier
					} else {
						actualType.humanReadableName + " to " + expectedType.humanReadableName
					}

				return new EObjectDiagnosticImpl(Severity.ERROR, IssueCodes.INCOMPATIBLE_TYPES,
					"Type mismatch: cannot convert from " + typePair, expression, null, -1, null);

				// end of customized code
			}
		} else {
			return new EObjectDiagnosticImpl(Severity.ERROR, IssueCodes.INCOMPATIBLE_TYPES,
				String.format("Type mismatch: type %s is not applicable at this location",
					actualType.humanReadableName), expression, null, -1, null);
		}
	}

}
