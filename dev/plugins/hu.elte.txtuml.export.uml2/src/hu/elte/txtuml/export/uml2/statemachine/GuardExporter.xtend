package hu.elte.txtuml.export.uml2.statemachine

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.uml2.uml.Constraint
import org.eclipse.uml2.uml.Transition

class GuardExporter extends Exporter<MethodDeclaration, IMethodBinding, Constraint> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createConstraint }

	override exportContents(MethodDeclaration source) {
		val opaqueExpr = factory.createOpaqueExpression
		result.specification = opaqueExpr
		result.name = "guard_specification"
		opaqueExpr.behavior = exportSMActivity(source) [
			(result.owner as Transition).container.stateMachine.ownedBehaviors += it
		]
	}

}