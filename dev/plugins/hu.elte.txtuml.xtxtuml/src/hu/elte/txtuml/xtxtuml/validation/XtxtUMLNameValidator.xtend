package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumerationLiteral
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.XbasePackage

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLNameValidator extends AbstractXtxtUMLValidator {

	private static val reservedJavaWords = #["abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
		"class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally",
		"float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
		"null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
		"synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"];

	@Check
	def checkPackageNameIsNotReserved(TUFile file) {
		checkReservedError(file, file.name, TU_FILE__NAME, "package");
	}

	@Check
	def checkModelElementNameIsNotReserved(TUModelElement modelElement) {
		checkReservedError(modelElement, modelElement.name, TU_MODEL_ELEMENT__NAME, "model element");
	}

	@Check
	def checkEnumerationLiteralNameIsNotReserved(TUEnumerationLiteral enumLiteral) {
		checkReservedError(enumLiteral, enumLiteral.name, TU_ENUMERATION_LITERAL__NAME, "enumeration literal");
	}

	@Check
	def checkSignalAttributeNameIsNotReserved(TUSignalAttribute attribute) {
		checkReservedError(attribute, attribute.name, TU_SIGNAL_ATTRIBUTE__NAME, "signal attribute");
	}

	@Check
	def checkAttributeNameIsNotReserved(TUAttribute attribute) {
		checkReservedError(attribute, attribute.name, TU_ATTRIBUTE__NAME, "attribute");
	}

	@Check
	def checkConstructorNameIsNotReserved(TUConstructor constructor) {
		checkReservedError(constructor, constructor.name, TU_CONSTRUCTOR__NAME, "constructor");
	}

	@Check
	def checkOperationNameIsNotReserved(TUOperation operation) {
		checkReservedError(operation, operation.name, TU_OPERATION__NAME, "operation");
	}

	@Check
	def checkStateNameIsNotReserved(TUState state) {
		checkReservedError(state, state.name, TU_STATE__NAME, "state");
	}

	@Check
	def checkTransitionNameIsNotReserved(TUTransition transition) {
		checkReservedError(transition, transition.name, TU_TRANSITION__NAME, "transition");
	}

	@Check
	def checkPortNameIsNotReserved(TUPort port) {
		checkReservedError(port, port.name, TU_CONNECTIVE_END__NAME, "port");
	}

	@Check
	def checkAssociationEndNameIsNotReserved(TUAssociationEnd associationEnd) {
		checkReservedError(associationEnd, associationEnd.name, TU_CONNECTIVE_END__NAME, "association end");
	}

	@Check
	def checkConnectorEndNameIsNotReserved(TUConnectorEnd connectorEnd) {
		checkReservedError(connectorEnd, connectorEnd.name, TU_CONNECTIVE_END__NAME, "connector end");
	}

	@Check
	def checkParameterNameIsNotReserved(JvmFormalParameter parameter) {
		checkReservedError(parameter, parameter.name, TypesPackage.Literals.JVM_FORMAL_PARAMETER__NAME, "parameter");
	}

	@Check
	def checkVariableNameIsNotReserved(XVariableDeclaration variable) {
		checkReservedError(variable, variable.name, XbasePackage.Literals.XVARIABLE_DECLARATION__NAME, "variable");
	}

	def protected checkReservedError(EObject element, String name, EStructuralFeature nameFeature, String type) {
		if (name.isReserved) {
			error(reservedErrorMessage(type, name), element, nameFeature, RESERVED_NAME);
		}
	}

	def protected isReserved(String name) {
		reservedJavaWords.contains(name)
	}

	def protected reservedErrorMessage(String type, String name) {
		"The name of " + type + " " + name + " is invalid – Java reserved words cannot be used as identifiers in txtUML"
	}

}
