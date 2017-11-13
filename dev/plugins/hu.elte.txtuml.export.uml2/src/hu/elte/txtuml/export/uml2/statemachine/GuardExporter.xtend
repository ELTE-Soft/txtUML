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
import java.util.Map
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment

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
	//TODO create guard code
	def String createFaltGuardExpressionCode(Block block) {
		/*val resCode = ""
		val varCodes = new HashMap<String,String>()
		val blockStatements = block.statements
		for(Object statement : blockStatements) {
			if(statement instanceof VariableDeclarationStatement) {
				val varDecl = statement as VariableDeclarationStatement;
				varDecl.fragments.forEach[
					val decl = it as VariableDeclarationFragment
					varCodes.put(decl.name.identifier, "")
				]
			}
			
		}
		
		for(Object statement : blockStatements) {
			if(statement instanceof Assignment) {
				val varDecl = statement as Assignment;
				val expr
			}
			
		}
				
		
		resCode*/
	}
	
	def StateMachine getSM(Region reg) { reg.stateMachine ?: reg.state.container.getSM() }

}