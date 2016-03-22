package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.Action

class ApiCallExporter extends ActionExporter<MethodInvocation, Action> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		switch access.resolveMethodBinding.name {
			case "assoc": factory.createReadLinkAction
			case "selectAny": factory.createSequenceNode
		}

	}

	override exportContents(MethodInvocation source) {
		switch source.resolveMethodBinding.name {
			case "assoc": {
				
			}
		}
	}

	

}
