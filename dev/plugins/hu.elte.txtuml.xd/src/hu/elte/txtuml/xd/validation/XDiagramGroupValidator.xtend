package hu.elte.txtuml.xd.validation

import hu.elte.txtuml.xd.xDiagramDefinition.XDGroupInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpressionList
import org.eclipse.xtext.validation.Check
import hu.elte.txtuml.api.layout.Diagram.NodeGroup
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.layout.Diagram.LinkGroup
import hu.elte.txtuml.api.model.Association
import hu.elte.txtuml.api.model.Composition
import hu.elte.txtuml.api.model.StateMachine

class XDiagramGroupValidator extends XDiagramPriorityValidator {
	@Check
	def checkGroupArgumentsConsistent(XDGroupInstruction group) {
		
		
		if (signature.diagramType == "state-machine-diagram") { // state machine diagram & co.
			val nodeArgsCount = group.^val.wrapped.expressions.filter[it.isSMDNodeType()].size();
			val linkArgsCount = group.^val.wrapped.expressions.filter[it.isSMDLinkType()].size();
			
			if (nodeArgsCount > 0 && linkArgsCount > 0){
				warning("a group may only contain arguments of LinkGroup, Transition or arguments of NodeGroup, Vertex parents", group, null);
			}
			
			if (nodeArgsCount == 0 && linkArgsCount > 0 && group.align != null){
				warning("group alignment should not be used on link groups", group, null)
			}
		} else if (signature.diagramType == "class-diagram") {
			val nodeArgsCount = group.^val.wrapped.expressions.filter[it.isCDNodeType()].size();
			val linkArgsCount = group.^val.wrapped.expressions.filter[it.isCDLinkType()].size();
			
			if (nodeArgsCount > 0 && linkArgsCount > 0){
				warning("a group may only contain arguments of LinkGroup, Association, Composition or arguments of NodeGroup, ModelClass parents", group, null);
			}

			if (nodeArgsCount == 0 && linkArgsCount > 0 && group.align != null){
				warning("group alignment should not be used on link groups", group, null)
			}
		}
	}
	
	def private boolean isSMDNodeType(XDTypeExpression exp){
		if (exp.phantom != null) return true;
		if (exp.name.checkSuperTypes(NodeGroup, StateMachine.Vertex)) return true;
		return false;
	}
	
	def private boolean isSMDLinkType(XDTypeExpression exp){
		if (exp.phantom != null) return false;
		if (exp.name.checkSuperTypes(LinkGroup, StateMachine.Transition)) return true;
		return false;
	}
	
	def private boolean isCDNodeType(XDTypeExpression exp){
		if (exp.phantom != null) return true;
		if (exp.name.checkSuperTypes(NodeGroup, ModelClass)) return true;
		return false;
	}
	
	def private boolean isCDLinkType(XDTypeExpression exp){
		if (exp.phantom != null) return false;
		if (exp.name.checkSuperTypes(LinkGroup, Association, Composition)) return true;
		return false;
	}
}
