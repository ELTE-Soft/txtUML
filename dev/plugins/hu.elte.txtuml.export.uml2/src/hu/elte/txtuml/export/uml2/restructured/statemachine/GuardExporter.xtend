package hu.elte.txtuml.export.uml2.restructured.statemachine

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.uml2.uml.Constraint
import org.eclipse.uml2.uml.Transition
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

class GuardExporter extends Exporter<MethodDeclaration, IMethodBinding, Constraint> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding access) { factory.createConstraint }

	override exportContents(MethodDeclaration source) {
		val opaqueExpr = factory.createOpaqueExpression
		opaqueExpr.behavior = exportActivity(source) [
			(result.owner as Transition).container.stateMachine.ownedBehaviors += it
		]
		opaqueExpr.name = "guard_expression"
		result.specification = opaqueExpr
		result.name = "guard_specification"
	}

}