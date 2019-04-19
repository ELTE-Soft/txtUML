package hu.elte.txtuml.xtxtuml.common

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnective
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectiveEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import org.eclipse.xtext.naming.IQualifiedNameProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDataType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal

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
	
	//isDataType
	def dispatch isDataType(TUDataType it){
		true
	}
	
	def dispatch isDataType(TUClass it){
		false
	}
	
	def dispatch isDataType(TUSignal it){
		false
	}
	
	//isClass
	def dispatch isClass(TUClass it){
		true
	}
	
	def dispatch isClass(TUDataType it){
		false
	}
	
	def dispatch isClass(TUSignal it){
		false
	}

	//isSignal
	def dispatch isSignal(TUSignal it){
		true
	}
	
	def dispatch isSignal(TUDataType it){
		false
	}
	
	def dispatch isSignal(TUClass it){
		false
	}	
}
