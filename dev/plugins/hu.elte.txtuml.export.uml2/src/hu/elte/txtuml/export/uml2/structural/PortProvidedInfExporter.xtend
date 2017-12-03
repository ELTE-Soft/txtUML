package hu.elte.txtuml.export.uml2.structural

import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.uml2.uml.Interface
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import java.util.Optional

class PortProvidedInfExporter extends Exporter<TypeDeclaration, ITypeBinding, Interface> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isInterface(typ)) factory.createInterface
	}
	
	override exportContents(TypeDeclaration td) {
		val provided = exportPortType(td.resolveBinding)		
		val port = (parent as PortExporter).result
		result.name = '''provided interface for «port.name»'''
		provided.ifPresent [
			val dummyInherit = factory.createGeneralization
			dummyInherit.specific = result
			dummyInherit.general = it
		]
	}
	
	def Optional<Interface> exportPortType(ITypeBinding iface) {
		if (iface.qualifiedName == hu.elte.txtuml.api.model.Interface.Empty.canonicalName) {
			Optional.empty
		} else {
			Optional.of(fetchType(iface) as Interface)
		}
	}
	
}