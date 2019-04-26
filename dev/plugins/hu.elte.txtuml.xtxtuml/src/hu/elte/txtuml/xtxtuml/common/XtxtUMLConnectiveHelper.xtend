package hu.elte.txtuml.xtxtuml.common

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnective
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectiveEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import org.eclipse.xtext.naming.IQualifiedNameProvider

/**
 * Helper class providing a common interface for connectives (currently
 * associations and connectors).
 */
class XtxtUMLConnectiveHelper {

	@Inject extension IQualifiedNameProvider;

	def connectiveName(TUConnective it) {
		fullyQualifiedName.lastSegment
	}

	def dispatch connectiveEnds(TUConnective it) {
		#[]
	}

	def dispatch connectiveEnds(TUAssociation it) {
		ends
	}

	def dispatch connectiveEnds(TUConnector it) {
		ends
	}

	def dispatch isDelegationConnector(TUConnective it) {
		false
	}

	def dispatch isDelegationConnector(TUConnector it) {
		delegation
	}

	def dispatch endEntity(TUConnectiveEnd it) {
		null
	}

	def dispatch endEntity(TUAssociationEnd it) {
		endClass
	}

	def dispatch endEntity(TUConnectorEnd it) {
		port
	}
}
