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
		if (exportActions) {
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
		if (blockStatements.exists[!(it instanceof VariableDeclarationStatement
				|| it instanceof ExpressionStatement || it instanceof ReturnStatement)]) {
			return guardExpressionSource
		}

		blockStatements.forEach[
			if (it instanceof VariableDeclarationStatement) {
				fragments.forEach[
					val decl = it as VariableDeclarationFragment
					localVariables.put(decl.name.identifier, decl.initializer)
				]
			}
		]

		blockStatements.filter[s | s instanceof ExpressionStatement
			&& (s as ExpressionStatement).expression instanceof Assignment
		].forEach[
			val assignment = (it as ExpressionStatement).expression as Assignment;
			val leftExpr = assignment.leftHandSide
			val rigthExpr = assignment.rightHandSide
			if (leftExpr instanceof SimpleName && ElementTypeTeller.isVariable(leftExpr)) {
				val leftVarName = leftExpr as SimpleName
				if (localVariables.containsKey(leftVarName.identifier)) {
					localVariables.put(leftVarName.identifier, rigthExpr)
				}
			}
		]

		if (localVariables.entrySet.exists[value instanceof Assignment]) {
			return guardExpressionSource
		}

		val retExpr = blockStatements.findFirst[it instanceof ReturnStatement] as ReturnStatement
		guardExpressionSource = asString(updateExpression(retExpr.expression, localVariables))

		return guardExpressionSource
	}

	def Expression updateExpression(Expression expr, Map<String,Expression> varCodes) {
		var resultExpr = expr
		switch resultExpr {
			SimpleName case varCodes.containsKey(resultExpr.identifier):
				return updateExpression(varCodes.get(resultExpr.identifier), varCodes)

			ParenthesizedExpression: {
				val updatedChild = updateExpression(resultExpr.expression, varCodes)
				if(updatedChild?.parent != resultExpr) {
					resultExpr.expression = updatedChild
				}
			}
			InfixExpression: {
				val updatedLeft = updateExpression(resultExpr.leftOperand, varCodes)
				val updatedRight = updateExpression(resultExpr.rightOperand, varCodes)
				if (updatedLeft != null && updatedLeft.parent != resultExpr) {
					updatedLeft.delete
					resultExpr.leftOperand = updatedLeft
				}
				if (updatedRight != null && updatedRight.parent != resultExpr) {
					updatedRight.delete
					resultExpr.rightOperand = updatedRight
				}
			}

			PrefixExpression: {
				val updatedExpr = updateExpression(resultExpr.operand, varCodes)
				if (updatedExpr?.parent != resultExpr) {
					updatedExpr?.delete
					resultExpr.operand = updatedExpr
				}
			}
			
			MethodInvocation: {
				val updatedExpr = updateExpression(resultExpr.expression, varCodes)
				if (updatedExpr?.parent != resultExpr) {
					updatedExpr?.delete
					resultExpr.expression = updatedExpr
				}

				val invArguments = resultExpr.arguments
				invArguments.forEach[arg, idx |
					var updatedArgument = updateExpression(arg as Expression, varCodes)
					if (updatedArgument?.parent != expr) {
						updatedArgument?.delete
						invArguments.set(idx, updatedArgument)
					}
				]
			}

			FieldAccess: {
				val updatedExpr = updateExpression(resultExpr.expression, varCodes)
				if (updatedExpr?.parent != resultExpr) {
					updatedExpr?.delete
					resultExpr.expression = updatedExpr
				}
			}
		}

		return resultExpr
	}

	def String asString(Expression expr) {
		switch expr {
			InfixExpression: {
				val leftCode = asString(expr.leftOperand)
				val rightCode = asString(expr.rightOperand)

				return leftCode + " " + expr.operator + " " + rightCode
			}

			ParenthesizedExpression: {
				return "(" + asString(expr.expression) + ")"
			}

			PrefixExpression: {
				return expr.operator + asString(expr.operand)
			}

			MethodInvocation: {
				val invName = expr.resolveMethodBinding.name
				if (invName == "getTrigger") {
					return "trigger"
				} else if (invName == "Else") {
					return "else";
				}

				val targetExpr = expr.expression
				var targetCode = targetExpressionAsString(targetExpr)

				var operationCode = expr.name.identifier
				var paramCodes = ""
				for (param : expr.arguments) {
					paramCodes += asString(param as Expression) + ", "
				}
				if (!paramCodes.empty) {
					paramCodes = paramCodes.substring(0, paramCodes.length - 2)
				}

				operationCode += "(" + paramCodes + ")"

				return targetCode + operationCode
			}

			ThisExpression: {
				return "this"
			}

			FieldAccess: {
				var targetCode = targetExpressionAsString(expr.expression)
				return targetCode + expr.name.identifier
			}
		}

		return expr.toString
	}

	def String targetExpressionAsString(Expression target) {
		var targetCode = ""
		if (target != null) {
			if (!(target instanceof ThisExpression)) {
				targetCode = asString(target)
			}
			if (!targetCode.empty) {
				targetCode += "."
			}
		}

		return targetCode
	}

	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}
