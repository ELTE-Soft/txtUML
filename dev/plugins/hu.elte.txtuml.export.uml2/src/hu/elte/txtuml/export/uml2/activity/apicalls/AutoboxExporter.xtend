package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SequenceNode

class AutoboxExporter extends ControlExporter<MethodInvocation, SequenceNode> {

	private val AUTOBOX_CLASSES = #[Boolean, Byte, Short, Integer, Long, Float, Double].map[canonicalName]

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (AUTOBOX_CLASSES.contains(access.resolveMethodBinding.declaringClass.qualifiedName) &&
			access.resolveMethodBinding.name == "valueOf" && access.arguments.size == 1)
			factory.createSequenceNode
	}

	override exportContents(MethodInvocation source) {
		val expr = exportExpression(source.arguments.get(0) as Expression)
		result.name = expr.name
	}
	
}
