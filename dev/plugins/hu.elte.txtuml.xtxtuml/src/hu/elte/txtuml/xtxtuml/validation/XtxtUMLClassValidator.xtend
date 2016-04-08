package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import java.util.HashSet
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

class XtxtUMLClassValidator extends XtxtUMLFileValidator {

	@Inject extension IQualifiedNameProvider;

	@Check
	def checkNoCycleInClassHiearchy(TUClass tUClass) {
		if (tUClass.superClass == null) {
			return;
		}

		val visitedClasses = new HashSet<TUClass>();
		visitedClasses.add(tUClass);

		var currentClass = tUClass.superClass;
		while (currentClass != null) {
			if (visitedClasses.contains(currentClass)) {
				error(
					"Cycle in hierarchy of class " + tUClass.name + " reaching " + currentClass.name,
					XtxtUMLPackage::eINSTANCE.TUClass_SuperClass,
					XtxtUMLIssueCodes.CLASS_HIERARCHY_CYCLE,
					currentClass.name
				);

				return;
			}

			visitedClasses.add(currentClass);
			currentClass = currentClass.superClass;
		}
	}

	@Check
	def checkElseGuard(TUTransitionGuard guard) {
		if (guard.^else && guard.eContainer instanceof TUTransition && (guard.eContainer as TUTransition).members.exists [
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from &&
				(it as TUTransitionVertex).vertex.type != TUStateType.CHOICE
		]) {
			error(
				"'else' condition can be used only if the source of the transition is a choice pseudostate",
				XtxtUMLPackage::eINSTANCE.TUTransitionGuard_Else
			)
		}
	}

	@Check
	def checkPortHaveAtMostOneInterfacePerType(TUPort port) {
		if (port.members.filter[required].length > 1 || port.members.filter[!required].length > 1) {
			error("Port " + port.name + " must not specify more than one required or provided interface", port,
				XtxtUMLPackage::eINSTANCE.TUClassProperty_Name, PORT_INTERFACE_COUNT_MISMATCH);
		}
	}

	@Check
	def checkTriggerPortIsBehavior(TUTransitionPort triggerPort) {
		if (!triggerPort.port.behavior) {
			error("Port " + triggerPort.port.name + " is not a behavior port", triggerPort,
				XtxtUMLPackage::eINSTANCE.TUTransitionPort_Port, TRIGGER_PORT_IS_NOT_BEHAVIOR, triggerPort.port.name)
		}
	}

	@Check
	def checkOwnerOfTriggerPort(TUTransitionPort triggerPort) {
		val containingClass = EcoreUtil2.getContainerOfType(triggerPort, TUClass); // due to composite states
		if (triggerPort.port.eContainer.fullyQualifiedName != containingClass.fullyQualifiedName) {
			error(triggerPort.port.name + " cannot be resolved as a port of class " + containingClass.name, triggerPort,
				XtxtUMLPackage::eINSTANCE.TUTransitionPort_Port, TRIGGER_PORT_OWNER_MISMATCH, triggerPort.port.name);
		}
	}

}
