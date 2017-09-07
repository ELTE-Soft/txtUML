package hu.elte.txtuml.export.uml2.activity.apicalls

import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.BaseExporter

class AssemblyConnectExporter extends ConnectExporterBase {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if (access.arguments.size == 4) {
			resultCreate(access)
		}
	}
	
	override exportContents(MethodInvocation source) {
		val p1 = source.arguments.get(1) as MethodInvocation
		val p2 = source.arguments.get(3) as MethodInvocation
		createAddToPortAction(p1,p2);
		createAddToPortAction(p2,p1);
	}
	
}