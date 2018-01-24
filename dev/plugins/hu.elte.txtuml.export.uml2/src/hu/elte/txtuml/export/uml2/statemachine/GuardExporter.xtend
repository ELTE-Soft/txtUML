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
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.jdt.core.dom.FieldAccess

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
		opaqueExpr.bodies += createFlatGuardExpressionCode(source.body)	
	}
	
	def String createFlatGuardExpressionCode(Block block) {
		
		val localVariables = new HashMap<String,Expression>()
		val blockStatements = block.statements
		
		var guardExpressionSource = "?"
		var c = blockStatements.stream.filter[s | !(s instanceof VariableDeclarationStatement) && 
			 !(s instanceof ExpressionStatement) && 
			 !(s instanceof ReturnStatement)].count	
		if(c > 0) {
			 	return guardExpressionSource
			 }
		
		blockStatements.stream.filter[s | s instanceof VariableDeclarationStatement].forEach[
				val varDecl = it as VariableDeclarationStatement;
				varDecl.fragments.forEach[
					val decl = it as VariableDeclarationFragment
					localVariables.put(decl.name.identifier, decl.initializer);
					
				]
		]
		
		blockStatements.stream.filter[s | s instanceof ExpressionStatement && 
			(s as ExpressionStatement).expression instanceof Assignment].
			forEach[
				val assignment = (it as ExpressionStatement).expression as Assignment;		
				val leftExpr = assignment.leftHandSide
				val rigthExpr = assignment.rightHandSide
				if(leftExpr instanceof SimpleName && ElementTypeTeller.isVariable(leftExpr)) {
					val leftVarName = leftExpr as SimpleName
					if(localVariables.containsKey(leftVarName.identifier)) {
						localVariables.put(leftVarName.identifier, rigthExpr)			
					}
				}
				
		]
		
		for(expr : localVariables.entrySet) {
			if(expr.value instanceof Assignment) {
				return guardExpressionSource
			}
		}
		
		
		val retExpr = blockStatements.findFirst[it instanceof ReturnStatement] as ReturnStatement
		guardExpressionSource = asString(updateExpression(retExpr.expression, localVariables))
			
		
		guardExpressionSource
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
			
		} else if (resultExpr instanceof FieldAccess) {
			val updatedExpr = updateExpression(resultExpr.expression, varCodes)
			if(updatedExpr?.parent != resultExpr) {
				updatedExpr?.delete
				resultExpr.expression = updatedExpr
			}
		}
		
		return resultExpr
				
	}
	
	def String asString(Expression expr) {
		if(expr instanceof InfixExpression) {
			
			val leftCode = asString(expr.leftOperand)
			val rigthCode = asString(expr.rightOperand)
			
			return leftCode + expr.operator + rigthCode
		} if(expr instanceof ParenthesizedExpression) {
			
			return asString(expr.expression)
			
		} else if(expr instanceof PrefixExpression) {
			
			return expr.operator + 	asString(expr.operand)	 
			 
		} else if(expr instanceof MethodInvocation) {
			
			val invName = expr.resolveMethodBinding.name
			if(invName == "getTrigger") {
				return "trigger"
			} else if(invName == "Else") {
				return "else()";
			}
			
			val targetExpr = expr.expression
			var targetCode = targetExpressionAsString(targetExpr)
			
			var operationCode = expr.name.identifier
			var paramCodes = ""
			for(param : expr.arguments) {
				paramCodes += asString(param as Expression) + ","
			}
			if(!paramCodes.empty) {
				paramCodes = paramCodes.substring(0, paramCodes.length-1)
			}
						
			operationCode += "(" + paramCodes + ")"
			
			return targetCode + operationCode
						
		} else if(expr instanceof ThisExpression) {	
				
			return "this"
						
		} else if(expr instanceof FieldAccess) {
			
			var targetCode = targetExpressionAsString(expr.expression)			
			return targetCode + expr.name.identifier
		}
		
		
		expr.toString
	}

	def String targetExpressionAsString(Expression target) {
		var targetCode = ""
		if(target != null) {
			if(!(target instanceof ThisExpression)) {
				targetCode = asString(target)
			}
			if(!targetCode.empty) {
				targetCode += "."
			}

		}
		
		targetCode
	}
	
	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}