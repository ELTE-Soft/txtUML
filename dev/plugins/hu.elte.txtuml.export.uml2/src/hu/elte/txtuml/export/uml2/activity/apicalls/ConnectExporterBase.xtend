package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.CreateLinkAction
import org.eclipse.uml2.uml.LinkEndData
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import hu.elte.txtuml.export.uml2.structural.ConnectorTypeEndExporter

abstract class ConnectExporterBase extends ActionExporter<MethodInvocation, CreateLinkAction>{
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	def createAddToPortAction(MethodInvocation targetPortReference, MethodInvocation sourcePortReference) {
		val targetRead = targetPortReference.exportExpression as ReadStructuralFeatureAction
		val sourceRead = sourcePortReference.exportExpression as ReadStructuralFeatureAction
		storeNode(targetRead)
		storeNode(sourceRead)
		
		val write = factory.createAddStructuralFeatureValueAction
		write.isReplaceAll = true
		write.structuralFeature = targetRead.structuralFeature
		write.name = '''«result.name».«write.structuralFeature.name»=«sourceRead.name»'''
		storeNode(write)
		val targetObj = exportExpression(targetPortReference.expression) ?: thisRef(targetRead.object.type)
		if (targetObj != null) {
			targetObj.result.objectFlow(write.createObject("targetObj", targetObj.result.type))
			sourceRead.result.objectFlow(write.createValue("new_port_value", sourceRead.result.type))
		
		}
	}
	
	protected def createEnd(LinkEndData end, ITypeBinding endType, MethodInvocation portReference) {
		
		end.end = fetchElement(endType, new ConnectorTypeEndExporter(this)) as Property
		val portRead = portReference.exportExpression as ReadStructuralFeatureAction
		end.value = result.createInputValue(portRead.name, end.end.type)
		portRead.objectFlow(end.value)
		return end
	}
	
	def resultCreate(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) 
			&& access.resolveMethodBinding.name == "connect"
		) {
			factory.createCreateLinkAction
		}
	}
	
	
}