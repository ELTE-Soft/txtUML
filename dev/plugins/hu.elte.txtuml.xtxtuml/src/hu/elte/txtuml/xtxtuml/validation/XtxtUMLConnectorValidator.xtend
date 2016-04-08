package hu.elte.txtuml.xtxtuml.validation

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

class XtxtUMLConnectorValidator extends XtxtUMLAssociationValidator {

	@Inject extension IQualifiedNameProvider;

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
				error(
					"Delegation connector " + connector.name + " must have exactly one container role",
					connector,
					XtxtUMLPackage.eINSTANCE.TUModelElement_Name,
					CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH,
					connector.name
				);
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

		val containerOfRoleA = if(roleA.eContainer instanceof TUComposition) roleA.eContainer as TUComposition;
		val containerOfRoleB = if(roleB.eContainer instanceof TUComposition) roleB.eContainer as TUComposition;

		if (connector.delegation) {
			if (containerOfRoleA == null ||
				containerOfRoleA.fullyQualifiedName != containerOfRoleB?.fullyQualifiedName) {
				error("Delegation connector " + connector.name +
					" must connect ports of a component and one of its parts", connector,
					XtxtUMLPackage::eINSTANCE.TUModelElement_Name, CONNECTOR_ROLE_COMPOSITION_MISMATCH, connector.name);
			}
		} else { // assembly connector
			if (containerOfRoleA == null || containerOfRoleB == null // roles must be from compositions
			|| containerOfRoleA.fullyQualifiedName == containerOfRoleB.fullyQualifiedName // underlying compositions must be different
			|| containerOfRoleA.ends.findFirst[container]?.endClass?.fullyQualifiedName !=
				containerOfRoleB.ends.findFirst[container]?.endClass?.fullyQualifiedName // container must be the same
			) {
				error("Assembly connector " + connector.name +
					" must connect ports of parts belonging to the same component", connector,
					XtxtUMLPackage::eINSTANCE.TUModelElement_Name, CONNECTOR_ROLE_COMPOSITION_MISMATCH, connector.name);
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

		val requiredAName = portA.getInterface(true)?.fullyQualifiedName;
		val requiredBName = portB.getInterface(true)?.fullyQualifiedName;
		val providedAName = portA.getInterface(false)?.fullyQualifiedName;
		val providedBName = portB.getInterface(false)?.fullyQualifiedName;

		if (connector.delegation && (requiredAName != requiredBName || providedAName != providedBName) ||
			!connector.delegation && (requiredAName != providedBName || providedAName != requiredBName)) {
			error("Connector " + connector.name + " connects incompatible ports", connector,
				XtxtUMLPackage::eINSTANCE.TUModelElement_Name, CONNECTOR_INCOMPATIBLE_PORTS, connector.name);
		}
	}

	def protected getInterface(TUPort port, boolean ofTypeRequired) {
		port.members.findFirst[required == ofTypeRequired]?.interface
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

}
