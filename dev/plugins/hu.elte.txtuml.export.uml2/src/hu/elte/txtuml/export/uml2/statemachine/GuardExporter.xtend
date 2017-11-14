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
import org.eclipse.jdt.core.dom.Name
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ReturnStatement
import java.util.Map
import java.util.Set
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.SimpleName

class GuardExporter extends Exporter<MethodDeclaration, IMethodBinding, Constraint> {
	
	private Expression guardExpression
	
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
		createFaltGuardExpressionCode(source.body)
		opaqueExpr.bodies += guardExpression.toString
	}
	
	// TODO need a better solution, works only special cases.
	def void createFaltGuardExpressionCode(Block block) {
		
		val localVariables = new HashMap<String,Expression>()
		val blockStatements = block.statements
		for(Object statement : blockStatements) {
			if(statement instanceof VariableDeclarationStatement) {
				val varDecl = statement as VariableDeclarationStatement;
				varDecl.fragments.forEach[
					val decl = it as VariableDeclarationFragment
					localVariables.put(decl.name.identifier, decl.initializer)
				]
			}
			
		}
		
		for(Object statement : blockStatements) {
			
			if(statement instanceof Assignment) {
				
				val assignment = statement as Assignment;
				val leftExpr = assignment.leftHandSide
				val rigthExpr = assignment.rightHandSide
				
				if(leftExpr instanceof SimpleName && ElementTypeTeller.isVariable(leftExpr)) {
					val leftVarName = leftExpr as SimpleName;
					if(localVariables.containsKey(leftVarName)) {
						localVariables.put(leftVarName.identifier, rigthExpr)
					}
				}
			}
			
		}
		
		
		for(Object statement : blockStatements) {	
			if(statement instanceof ReturnStatement) { 
				guardExpression = statement.expression
				guardExpression = updateExpression(guardExpression, localVariables)

			}
		}
		
	}
	
	
	def Expression updateExpression(Expression expr, Map<String,Expression> varCodes) {
		var resultExpr = expr
		//TODO consider more possible expression type
		if(resultExpr instanceof SimpleName && varCodes.containsKey((resultExpr as SimpleName).identifier)) {
			resultExpr = updateExpression(varCodes.get((resultExpr as SimpleName).identifier), varCodes)
		} else if(resultExpr instanceof  InfixExpression) {
			val updatedLeft =  updateExpression(resultExpr.leftOperand, varCodes)
			val updadtedRigthright = updateExpression(resultExpr.rightOperand, varCodes)
			updatedLeft.delete
			updadtedRigthright.delete
			resultExpr.leftOperand = updatedLeft
			resultExpr.rightOperand = updadtedRigthright
		}
		
		resultExpr
		
		
	}
	
	
	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}