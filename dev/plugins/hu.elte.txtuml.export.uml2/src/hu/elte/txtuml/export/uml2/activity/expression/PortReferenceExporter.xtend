package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.Port

class PortReferenceExporter extends ActionExporter<MethodInvocation, ReadStructuralFeatureAction>{
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(MethodInvocation access) {
		if(access.resolveMethodBinding.name == "port") {
			factory.createReadStructuralFeatureAction
		}
	}
	
	override exportContents(MethodInvocation source) {
		val portType = source.arguments.get(0) as TypeLiteral
		val portTypeBinding = portType.type.resolveBinding
		val target = exportExpression(source.expression) ?: thisRef(portTypeBinding.declaringClass.fetchType)
		result.name = '''read «portTypeBinding.name» '''
		target.result.objectFlow(result.createObject("this", target.result.type))	
		if(portTypeBinding != null) {
			var portElement = fetchElement(portTypeBinding) as Port
			
			result.structuralFeature = portElement
			result.createResult(result.name, portElement.type)
			
		}
	}
	
}