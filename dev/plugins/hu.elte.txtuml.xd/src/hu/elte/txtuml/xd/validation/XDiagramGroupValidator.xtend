package hu.elte.txtuml.xd.validation

import hu.elte.txtuml.api.layout.Diagram.LinkGroup
import hu.elte.txtuml.api.layout.Diagram.NodeGroup
import hu.elte.txtuml.xd.xDiagramDefinition.GroupInstruction
import org.eclipse.xtext.validation.Check
import hu.elte.txtuml.api.model.StateMachine.Vertex
import hu.elte.txtuml.api.model.StateMachine.Transition

class XDiagramGroupValidator extends XDiagramPriorityValidator {
	@Check
	def checkGroupArgumentsConsistent(GroupInstruction group) {
//		if (!group.validateArgumentSuperTypes(Vertex, NodeGroup) || !group.validateArgumentSuperTypes(Transition, LinkGroup)) {
//			error("oh noes.", group, null);
//		}
		if (signature.genArg != null) { // state machine diagram & co.
			
		}
//		group.^val.wrapped.expressions.forEach[name.checkSuperTypes(Vertex, NodeGroup)]
	}
}
