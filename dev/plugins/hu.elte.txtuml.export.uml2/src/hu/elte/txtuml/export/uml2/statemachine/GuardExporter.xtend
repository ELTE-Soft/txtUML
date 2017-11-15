package hu.elte.txtuml.export.uml2.statemachine

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.uml2.uml.Constraint
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.Region
import org.eclipse.uml2.uml.StateMachine
import org.eclipse.jdt.core.dom.Block
import java.util.HashMap
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ReturnStatement
import java.util.Map
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.MethodInvocation

class GuardExporter extends Exporter<MethodDeclaration, IMethodBinding, Constraint> {
	
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createConstraint }
	
	override exportContents(MethodDeclaration source) {
		val opaqueExpr = factory.createOpaqueExpression
		result.specification = opaqueExpr
		result.name = "guard_specification"
		if(exportActions) {
			opaqueExpr.behavior = exportSMActivity(source) [
			(result.owner as Transition).container.getSM.ownedBehaviors += it
			]
		}

		opaqueExpr.languages += "JAVA"
		opaqueExpr.bodies += createFaltGuardExpressionCode(source.body)	
	}
	
	def String createFaltGuardExpressionCode(Block block) {
		
		val localVariables = new HashMap<String,Expression>()
		val blockStatements = block.statements
		
		var guardExpressionSource = "error"
		var c = blockStatements.stream.filter[s | !(s instanceof VariableDeclarationStatement) && 
			 !(s instanceof Assignment) && 
			 !(s instanceof ReturnStatement)].count	
		if(c > 0) {
			 	return guardExpressionSource
			 }
		
		blockStatements.stream.filter[s | s instanceof VariableDeclarationStatement].forEach[
				val varDecl = it as VariableDeclarationStatement;
				varDecl.fragments.forEach[
					val decl = it as VariableDeclarationFragment
					localVariables.put(decl.name.identifier, decl.initializer)
				]
		]
		
		blockStatements.stream.filter[s | s instanceof Assignment].forEach[
				val assignment = it as Assignment;
				val leftExpr = assignment.leftHandSide
				val rigthExpr = assignment.rightHandSide
				
				if(leftExpr instanceof SimpleName && ElementTypeTeller.isVariable(leftExpr)) {
					val leftVarName = leftExpr as SimpleName;
					if(localVariables.containsKey(leftVarName)) {
						localVariables.put(leftVarName.identifier, rigthExpr)
					}
				}
		]
		
		for(Object statement : blockStatements) {	
			if(statement instanceof ReturnStatement) { 
				guardExpressionSource = updateExpression(statement.expression, localVariables).toString
				

			}
		}
		
		return guardExpressionSource
	}
	
	
	def Expression updateExpression(Expression expr, Map<String,Expression> varCodes) {
		var resultExpr = expr
		if(resultExpr instanceof SimpleName && varCodes.containsKey((resultExpr as SimpleName).identifier)) {
			resultExpr = updateExpression(varCodes.get((resultExpr as SimpleName).identifier), varCodes)
		} else if(resultExpr instanceof InfixExpression) {
			val updatedLeft =  updateExpression(resultExpr.leftOperand, varCodes)
			val updadtedRight = updateExpression(resultExpr.rightOperand, varCodes)
			if(updatedLeft?.parent != resultExpr) { 
				updatedLeft?.delete
				resultExpr.leftOperand = updatedLeft
			}
			if(updadtedRight?.parent != resultExpr) {
				updadtedRight?.delete
				resultExpr.rightOperand = updadtedRight
				
			} 
		} else if(resultExpr instanceof PrefixExpression) {
			val updatedExpr = updateExpression(resultExpr.operand, varCodes)		
			if(updatedExpr?.parent != resultExpr) {
				updatedExpr?.delete
				resultExpr.operand = updatedExpr
			}			 
			 
		} else if(resultExpr instanceof MethodInvocation) {
			val updatedExpr = updateExpression(resultExpr.expression, varCodes)
			if(updatedExpr?.parent != resultExpr) {
				updatedExpr?.delete
				resultExpr.expression = updatedExpr
			}
			for(var i = 0; i < resultExpr.arguments.size; i++) {
				var updatedArgument = updateExpression(resultExpr.arguments.get(i) as Expression,varCodes)
				if(updatedArgument?.parent != resultExpr) {
					updatedArgument?.delete
					resultExpr.arguments.set(i, updatedArgument)
				}
			}		
			
		}
		
		return resultExpr
		
		
	}

	
	
	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}