package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.SequenceNode

@Deprecated // Remove this when all api calls are implemented
class IgnoredAPICallExporter extends ControlExporter<MethodInvocation, SequenceNode> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if(isApiMethodInvocation(access.resolveMethodBinding)) factory.createSequenceNode
	}

	override exportContents(MethodInvocation source) {
		val methodBind = source.resolveMethodBinding
		result.name = '''«methodBind.declaringClass.qualifiedName».«methodBind.name»'''
	}

}