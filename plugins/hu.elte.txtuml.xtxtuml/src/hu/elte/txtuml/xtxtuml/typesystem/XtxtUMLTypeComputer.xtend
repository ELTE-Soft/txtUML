package hu.elte.txtuml.xtxtuml.typesystem

import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceFlags
import org.eclipse.xtext.xbase.XVariableDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression

class XtxtUMLTypeComputer extends XbaseWithAnnotationsTypeComputer {
	
	def dispatch computeTypes(RAlfSendSignalExpression sendExpr, ITypeComputationState state) {
		super.computeTypes(sendExpr.signal, state);
		super.computeTypes(sendExpr.target, state);
		state.acceptActualType(state.getPrimitiveVoid);
	}
	
	def dispatch computeTypes(RAlfDeleteObjectExpression deleteExpr, ITypeComputationState state) {
		super.computeTypes(deleteExpr.object, state);
		state.acceptActualType(state.getPrimitiveVoid);
	}
	
	override dispatch computeTypes(XBlockExpression block, ITypeComputationState state) {
		val children = block.expressions;
   		if (!children.isEmpty) {
   			state.withinScope(block);
   		}
   		
		for (child : children) {
			val expressionState = state.withoutExpectation; // no expectation
			expressionState.computeTypes(child);
			if (child instanceof XVariableDeclaration) {
				addLocalToCurrentScope(child as XVariableDeclaration, state);
			}
		}
   		
		for (expectation : state.expectations) {
			expectation.acceptActualType(
				expectation.referenceOwner.newAnyTypeReference,
				ConformanceFlags.UNCHECKED
			);
		}
	}
	
}