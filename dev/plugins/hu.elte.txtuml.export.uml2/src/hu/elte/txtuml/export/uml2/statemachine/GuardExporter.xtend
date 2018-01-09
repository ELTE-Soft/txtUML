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
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import java.util.List
import java.util.ArrayList

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
		
		var guardExpressionSource = "?"
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
					localVariables.put(decl.name.identifier, decl.initializer);
					 if(decl.initializer instanceof Assignment) {
						val assignment = decl.initializer as Assignment;		
						var identifiers = new ArrayList<String>()
						val assignChainResult = obtainAssigmentExpression(assignment, identifiers, localVariables);
						identifiers.forEach[
							if(localVariables.containsKey(it)) {
								localVariables.put(it, assignChainResult)
							}
						]
						localVariables.put(decl.name.identifier,assignChainResult)
					}
					
				]
		]
		
		blockStatements.stream.filter[s | s instanceof Assignment].forEach[
				val assignment = it as Assignment;		
				var identifiers = new ArrayList<String>()
				val assignChainResult = obtainAssigmentExpression(assignment, identifiers, localVariables);
				identifiers.forEach[
					if(localVariables.containsKey(it)) {
						localVariables.put(it, assignChainResult)
					}
				]
		]
		val retExpr = blockStatements.findFirst[it instanceof ReturnStatement] as ReturnStatement
		guardExpressionSource = updateExpression(retExpr.expression, localVariables).toString
			
		
		return guardExpressionSource
	}
	
	def Expression obtainAssigmentExpression(Assignment assignment, List<String> identifiers, Map<String,Expression> localVariables) {
			
			val leftExpr = assignment.leftHandSide
			val rigthExpr = assignment.rightHandSide
			if(leftExpr instanceof SimpleName && ElementTypeTeller.isVariable(leftExpr)) {
					val leftVarName = leftExpr as SimpleName
					if(localVariables.containsKey(leftVarName.identifier)) {
						identifiers.add(leftVarName.identifier)			
					}
					if(!(rigthExpr instanceof Assignment)) {
						return rigthExpr
					} else {
						return obtainAssigmentExpression(rigthExpr as Assignment, identifiers, localVariables);
					}

				}
			assignment
	}
	def Expression updateExpression(Expression expr, Map<String,Expression> varCodes) {
		var resultExpr = expr
		if(resultExpr instanceof SimpleName && varCodes.containsKey((resultExpr as SimpleName).identifier)) {
			resultExpr = updateExpression(varCodes.get((resultExpr as SimpleName).identifier), varCodes)
		} else if(resultExpr instanceof ParenthesizedExpression) {
			resultExpr = updateExpression(resultExpr.expression,varCodes)
		}
		else if(resultExpr instanceof InfixExpression) {
			val updatedLeft =  updateExpression(resultExpr.leftOperand, varCodes)
			val updadtedRight = updateExpression(resultExpr.rightOperand, varCodes)			
			if(updatedLeft != null && updatedLeft.parent != resultExpr) { 
				updatedLeft.delete
				resultExpr.leftOperand = updatedLeft
			}
			if(updadtedRight != null && updadtedRight.parent != resultExpr) {
				updadtedRight.delete
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