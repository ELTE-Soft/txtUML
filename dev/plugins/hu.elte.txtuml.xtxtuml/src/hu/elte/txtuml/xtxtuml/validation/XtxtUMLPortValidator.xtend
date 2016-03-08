package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

class XtxtUMLPortValidator extends XtxtUMLAssociationValidator {

	public static val CONNECTOR_ASSEMBLY_CONTAINS_CONTAINER_ROLE = "hu.elte.txtuml.xtxtuml.issues.ConnectorAssemblyContainsContainerRole";
	public static val CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorContainerRoleCountMismatch";
	public static val CONNECTOR_INCOMPATIBLE_PORTS = "hu.elte.txtuml.xtxtuml.ConnectorIncompatiblePorts";
	public static val CONNECTOR_ROLE_COMPOSITION_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorRoleCompositionMismatch";

	public static val CONNECTOR_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndCountMismatch";
	public static val CONNECTOR_END_DUPLICATE = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndDuplicate";
	public static val CONNECTOR_END_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndPortOwnerMismatch";

	public static val PORT_INTERFACE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.PortInterfaceCountMismatch";
	public static val PORT_NAME_IS_NOT_UNIQUE = "hu.elte.txtuml.xtxtuml.issues.PortNameIsNotUnique";

	public static val TRIGGER_PORT_IS_NOT_BEHAVIOR = "hu.elte.txtuml.xtxtuml.issues.TriggerPortIsNotBehavior";
	public static val TRIGGER_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.TriggerPortOwnerMismatch";

	@Inject extension IQualifiedNameProvider

	@Check
	def checkPortHaveAtMostOneInterfacePerType(TUPort port) {
		if (port.members.filter[provided].length > 1 || port.members.filter[!provided].length > 1) {
			error("Port " + port.name + " must not specify more than one provided or required interface", port,
				XtxtUMLPackage::eINSTANCE.TUClassProperty_Name, PORT_INTERFACE_COUNT_MISMATCH);
		}
	}

	@Check
	def checkConnectorHaveExactlyTwoEnds(TUConnector connector) {
		val endCount = connector.ends.length;
		if (endCount != 2) {
			error("Connector " + connector.name + " must have exactly two ends", connector,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name, CONNECTOR_END_COUNT_MISMATCH, endCount.toString);
		}
	}

	@Check
	def checkContainerEndIsAllowedAndNeededOnlyInDelegation(TUConnector connector) {
		val containerEnds = connector.ends.filter[role.isContainer];
		if (connector.delegation) {
			if (1 != containerEnds.length) {
				error("Delegation connector " + connector.name + " must have exactly one container role", connector,
					XtxtUMLPackage.eINSTANCE.TUModelElement_Name, CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH,
					connector.name);
			}
		} else {
			containerEnds.forEach [
				error("Container role " + it.role.name + " of connector end " + it.name +
					" must not be present in an assembly connector", it, XtxtUMLPackage.eINSTANCE.TUConnectorEnd_Role,
					CONNECTOR_ASSEMBLY_CONTAINS_CONTAINER_ROLE, it.role.name);
			]
		}
	}

	@Check
	def checkCompositionsBehindConnectorEnds(TUConnector connector) {
		if (connector.ends.size != 2) {
			return;
		}

		val roleA = connector.ends.get(0).role;
		val roleB = connector.ends.get(1).role;

		val containerOfRoleA = if (roleA.eContainer instanceof TUComposition) roleA.eContainer as TUComposition;
		val containerOfRoleB = if (roleB.eContainer instanceof TUComposition) roleB.eContainer as TUComposition;

		if (connector.delegation) {
			if (containerOfRoleA == null || containerOfRoleA.fullyQualifiedName != containerOfRoleB?.fullyQualifiedName) {
				error("Delegation connector " + connector.name + " must connect ports of a component and one of its parts",
					connector, XtxtUMLPackage::eINSTANCE.TUModelElement_Name,
					CONNECTOR_ROLE_COMPOSITION_MISMATCH, connector.name);
			}
		} else { // assembly connector
			if (containerOfRoleA == null || containerOfRoleB == null // roles must be from compositions
			|| containerOfRoleA.fullyQualifiedName == containerOfRoleB.fullyQualifiedName // underlying compositions must be different
			|| containerOfRoleA.ends.findFirst[container]?.endClass?.fullyQualifiedName !=
				containerOfRoleB.ends.findFirst[container]?.endClass?.fullyQualifiedName // container must be the same
			) {
				error("Assembly connector " + connector.name + " must connect ports of parts belonging to the same component",
					connector, XtxtUMLPackage::eINSTANCE.TUModelElement_Name,
					CONNECTOR_ROLE_COMPOSITION_MISMATCH, connector.name);
			}
		}
	}

	@Check
	def checkConnectorEndPortCompatibility(TUConnector connector) {
		if (connector.ends.size != 2) {
			return;
		}

		val portA = connector.ends.get(0).port;
		val portB = connector.ends.get(1).port;

		val providedAName = portA.getInterface(true)?.fullyQualifiedName;
		val providedBName = portB.getInterface(true)?.fullyQualifiedName;
		val requiredAName = portA.getInterface(false)?.fullyQualifiedName;
		val requiredBName = portB.getInterface(false)?.fullyQualifiedName;

		if (connector.delegation && (providedAName != providedBName || requiredAName != requiredBName)
		|| !connector.delegation && (providedAName != requiredBName || requiredAName != providedBName)
		) {
			error("Connector " + connector.name + " connects incompatible ports", connector,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name, CONNECTOR_INCOMPATIBLE_PORTS, connector.name);
		}
	}

	def private getInterface(TUPort port, boolean ofTypeProvided) {
		port.members.findFirst[provided == ofTypeProvided]?.interface
	}

	@Check
	def checkOwnerOfConnectorEndPort(TUConnectorEnd connEnd) {
		val ownerOfPort = connEnd.port.eContainer;
		val classInRole = connEnd.role.endClass;

		if (ownerOfPort.fullyQualifiedName != classInRole.fullyQualifiedName) {
			error(connEnd.port.name + " cannot be resolved as a port of class " + classInRole.name, connEnd,
				XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Port, CONNECTOR_END_PORT_OWNER_MISMATCH, connEnd.port.name);
		}
	}

	@Check
	def checkNoDuplicateConnectorEnd(TUConnectorEnd connEnd) {
		val container = connEnd.eContainer as TUConnector;
		if (container.ends.exists[(it.name == connEnd.name || it.role.fullyQualifiedName == connEnd.role.fullyQualifiedName)
			&& it != connEnd // direct comparison is safe here
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
			|| it instanceof TUState && (it as TUState).name == port.name
			|| it instanceof TUTransition && (it as TUTransition).name == port.name
		]) {
			error("Port " + port.name + " in class " + containingClass.name +
				" must have a unique name among states, transitions and ports of the enclosing element", port,
				XtxtUMLPackage::eINSTANCE.TUClassProperty_Name, PORT_NAME_IS_NOT_UNIQUE, port.name);
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
