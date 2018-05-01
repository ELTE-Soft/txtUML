package hu.elte.txtuml.export.uml2.structural

import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.uml2.uml.Reception
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.MethodDeclaration
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Signal

class ReceptionExporter extends Exporter<MethodDeclaration, IMethodBinding, Reception> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(IMethodBinding access) {
		factory.createReception
	}
	
	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name		
		result.signal = (fetchType(binding.parameterTypes.get(0)) as Signal)
		result.ownedParameters += decl.parameters.map [
			exportParameter((it as SingleVariableDeclaration).resolveBinding) 
		]
	}
	
}
