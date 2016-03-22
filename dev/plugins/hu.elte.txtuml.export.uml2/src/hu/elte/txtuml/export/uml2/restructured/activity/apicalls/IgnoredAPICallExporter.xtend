package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SequenceNode

class IgnoredAPICallExporter extends ActionExporter<MethodInvocation, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		factory.createSequenceNode
	}

	override exportContents(MethodInvocation source) {
		val methodBind = source.resolveMethodBinding
		result.name = '''«methodBind.declaringClass.qualifiedName».«methodBind.name»'''
	}

}