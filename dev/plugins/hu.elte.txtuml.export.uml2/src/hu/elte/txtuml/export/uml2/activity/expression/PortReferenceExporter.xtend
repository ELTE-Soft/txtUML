package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import org.eclipse.jdt.core.dom.ExpressionMethodReference
import hu.elte.txtuml.export.uml2.BaseExporter
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import hu.elte.txtuml.export.uml2.structural.PortExporter
import org.eclipse.uml2.uml.Port

class PortReferenceExporter extends ActionExporter<ExpressionMethodReference, ReadStructuralFeatureAction>{
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ExpressionMethodReference access) {
		factory.createReadStructuralFeatureAction
	}
	
	override exportContents(ExpressionMethodReference source) {
		val fieldAcces = source.expression as FieldAccess
		val methodInv = fieldAcces.expression as MethodInvocation
		val portType = methodInv.arguments.get(0) as TypeLiteral
		val portTypeBinding = portType.type.resolveBinding
		val portDeclaringClass = portTypeBinding.declaringClass
		val thisTarget = thisRef(portDeclaringClass.fetchType)
		result.name = '''read «portTypeBinding.name» '''
		thisTarget.result.objectFlow(result.createObject("this", thisTarget.result.type))	
		if(portTypeBinding != null) {
			val portElement = fetchElement(portTypeBinding, new PortExporter(this)) as Port
			result.structuralFeature = portElement
			result.createResult(result.name, portElement.type)
			
		}
	}
	
}