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

class GuardExporter extends Exporter<MethodDeclaration, IMethodBinding, Constraint> {
	
	private String guardCode
	
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
		guardCode = ""
		createFaltGuardExpressionCode(source.body)
		opaqueExpr.bodies += guardCode
	}
	
	// TODO need a more elegant solution..
	def void createFaltGuardExpressionCode(Block block) {
		
		val localVariables = new HashMap<String,String>()
		val blockStatements = block.statements
		for(Object statement : blockStatements) {
			if(statement instanceof VariableDeclarationStatement) {
				val varDecl = statement as VariableDeclarationStatement;
				varDecl.fragments.forEach[
					val decl = it as VariableDeclarationFragment
					localVariables.put(decl.name.identifier, "")
				]
			}
			
		}
		
		for(Object statement : blockStatements) {
			
			if(statement instanceof Assignment) {
				
				val assignment = statement as Assignment;
				val leftExpr = assignment.leftHandSide
				val rigthExpr = assignment.rightHandSide
				
				if(leftExpr instanceof Name && ElementTypeTeller.isVariable(leftExpr)) {
					val leftVarName = (leftExpr as Name).resolveBinding.name;
					if(localVariables.containsKey(leftVarName)) {
						localVariables.put(leftVarName, rigthExpr.toString)
					}
				}
			}
			
		}
		
		
		for(Object statement : blockStatements) {	
			if(statement instanceof ReturnStatement) { 
				guardCode = statement.toString
				resolveExpressionCode(localVariables)
			}
		}
		
	}
	
	def void resolveExpressionCode(Map<String,String> varCodes) {
		val varNames = varCodes.keySet
		while(containsAnyOfThem(guardCode, varNames)) {
			guardCode = replaceToVarExpressions(guardCode, varCodes)
		}
		
	}
	
	def boolean containsAnyOfThem(String code, Set<String> strings) {
		for(String  varName : strings) {
			if(code.contains(varName)) {
				return true;
			}
		}
		
		return false;
	}
	
	def String replaceToVarExpressions(String code, Map<String,String> varCodes) {
		val res = code
		for(String varName : varCodes.keySet) {
			if(code.contains(varName)) {
				res.replace(varName, varCodes.get(varName))
			}
		}
		
		res
	}
	
	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}