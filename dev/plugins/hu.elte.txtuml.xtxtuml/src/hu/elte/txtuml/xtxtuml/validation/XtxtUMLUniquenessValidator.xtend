package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

class XtxtUMLUniquenessValidator extends AbstractXtxtUMLValidator {

	@Inject extension IQualifiedNameProvider;

	@Check
	def checkNoDuplicateFileElementExternal(TUModelElement modelElement) {
		try {
			Class.forName(modelElement.fullyQualifiedName.toString, false, getClass.getClassLoader);

			// class with the same qualified name is found
			error(
				"Duplicate file element " + modelElement.name,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name
			);
		} catch (ClassNotFoundException ex) {
			// no problem
		}
	}

	@Check
	def CheckNoDuplicateFileElementInternal(TUModelElement modelElement) {
		val siblingsAndSelf = (modelElement.eContainer as TUFile).elements;
		if (siblingsAndSelf.exists [
			name == modelElement.name && it != modelElement // direct comparison is safe here
		]) {
			error(
				"Duplicate file element " + modelElement.name,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name
			);
		}
	}

	/*
	 * TODO remove when general TUAttribute is introduced
	 * TODO override local variable shadowing check defined in AbstractTypeComputationState
	 */
	@Check
	def checkNoDuplicateSignalAttribute(TUSignalAttribute attr) {
		val containingSignal = attr.eContainer as TUSignal;
		if (containingSignal.attributes.exists [
			name == attr.name && it != attr // direct comparison is safe here
		]) {
			error(
				"Duplicate attribute " + attr.name + " in signal " + containingSignal.name,
				XtxtUMLPackage::eINSTANCE.TUSignalAttribute_Name
			);
		}
	}

	@Check
	def checkNoDuplicateAttribute(TUAttribute attr) {
		val containingClass = attr.eContainer as TUClass;
		if (containingClass.members.exists [
			it instanceof TUAttribute && (it as TUAttribute).name == attr.name && it != attr // direct comparison is safe here
		]) {
			error(
				"Duplicate attribute " + attr.name + " in class " + containingClass.name,
				XtxtUMLPackage::eINSTANCE.TUAttribute_Name
			);
		}
	}

	@Check
	def checkNoDuplicateOperation(TUOperation op) {
		val containingClass = (op.eContainer as TUClass);
		if (containingClass.members.exists [
			it instanceof TUOperation && {
				val siblingOrSelfOp = it as TUOperation;
				siblingOrSelfOp.name == op.name && siblingOrSelfOp.parameterTypeList == op.parameterTypeList
			} && it != op // direct comparison is safe here
		]) {
			error(
				'''Duplicate method «op.name»(«op.parameterTypeList.join(", ")»)''',
				XtxtUMLPackage.eINSTANCE.TUOperation_Name
			);
		}
	}

	@Check
	def checkStateNameIsUnique(TUState state) {
		val container = state.eContainer;
		var inClass = false;
		val siblingsAndSelf = if (container instanceof TUClass) {
				inClass = true;
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUState && (it as TUState).name == state.name && it != state || // direct comparison is safe here
			it instanceof TUTransition && (it as TUTransition).name == state.name ||
				it instanceof TUPort && (it as TUPort).name == state.name
		]) {
			error(
				"State " + state.name + " in " + (if (inClass)
					"class " + (container as TUClass).name
				else
					"state " + (container as TUState).name) +
					" must have a unique name among states, transitions and ports of the enclosing element",
				XtxtUMLPackage::eINSTANCE.TUState_Name
			);
		}
	}

	@Check
	def checkTransitionNameIsUnique(TUTransition trans) {
		val container = trans.eContainer;
		var inClass = false;
		val siblingsAndSelf = if (container instanceof TUClass) {
				inClass = true;
				(container as TUClass).members
			} else {
				(container as TUState).members
			}

		if (siblingsAndSelf.exists [
			it instanceof TUTransition && (it as TUTransition).name == trans.name && it != trans || // direct comparison is safe here
			it instanceof TUState && (it as TUState).name == trans.name ||
				it instanceof TUPort && (it as TUPort).name == trans.name
		]) {
			error(
				"Transition " + trans.name + " in " + (if (inClass)
					"class " + (container as TUClass).name
				else
					"state " + (container as TUState).name) +
					" must have a unique name among states, transitions and ports of the enclosing element",
				XtxtUMLPackage::eINSTANCE.TUTransition_Name
			);
		}

	}

	@Check
	def checkAssociationEndNamesAreUnique(TUAssociationEnd associationEnd) {
		val association = associationEnd.eContainer as TUAssociation
		if (1 < association.ends.filter[name == associationEnd.name].length) {
			error("Association end " + associationEnd.name + " in association " + association.name +
				" must have a unique name", associationEnd, XtxtUMLPackage.eINSTANCE.TUClassProperty_Name,
				ASSOCIATION_END_NAME_IS_NOT_UNIQUE, associationEnd.name)
		}
	}

	@Check
	def checkNoDuplicateConnectorEnd(TUConnectorEnd connEnd) {
		val container = connEnd.eContainer as TUConnector;
		if (container.ends.exists [
			(it.name == connEnd.name || it.role.fullyQualifiedName == connEnd.role.fullyQualifiedName) && it != connEnd // direct comparison is safe here
		]) {
			error("Duplicate connector end " + connEnd.name + " in connector " + container.name +
				". Names and roles must be unique among ends of a connector.", connEnd,
				XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Name, CONNECTOR_END_DUPLICATE, connEnd.name);
		}
	}

	@Check
	def checkPortNameIsUnique(TUPort port) {
		val containingClass = port.eContainer as TUClass;
		if (containingClass.members.exists [
			it instanceof TUPort && (it as TUPort).name == port.name && it != port // direct comparison is safe here
			|| it instanceof TUState && (it as TUState).name == port.name ||
				it instanceof TUTransition && (it as TUTransition).name == port.name
		]) {
			error("Port " + port.name + " in class " + containingClass.name +
				" must have a unique name among states, transitions and ports of the enclosing element", port,
				XtxtUMLPackage::eINSTANCE.TUClassProperty_Name, PORT_NAME_IS_NOT_UNIQUE, port.name);
		}
	}

	def protected parameterTypeList(TUOperation op) {
		op.parameters.map[parameterType.type.fullyQualifiedName]
	}

}
