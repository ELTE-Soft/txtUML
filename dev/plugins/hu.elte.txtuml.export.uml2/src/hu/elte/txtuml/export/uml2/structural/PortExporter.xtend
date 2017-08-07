package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.Port
import hu.elte.txtuml.export.uml2.BaseExporter
import java.util.Optional

public class PortExporter extends Exporter<TypeDeclaration, ITypeBinding, Port> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) { if(ElementTypeTeller.isPort(access)) factory.createPort }

	override exportContents(TypeDeclaration source) {
		val typeArguments = source.resolveBinding.superclass.typeArguments
		exportWithInterfaces(exportPortType(typeArguments.get(0)), exportPortType(typeArguments.get(1)),
			ElementTypeTeller.isBehavioralPort(source), source.name.identifier)
	}

	def Optional<Interface> exportPortType(ITypeBinding iface) {
		if (iface.qualifiedName == hu.elte.txtuml.api.model.Interface.Empty.canonicalName) {
			Optional.empty
		} else {
			Optional.of(fetchType(iface) as Interface)
		}
	}

	def exportWithInterfaces(Optional<Interface> provided, Optional<Interface> required, boolean isBehavior,
		String portName) {
		result.isBehavior = isBehavior;
		val dummyProvided = factory.createInterface
		dummyProvided.name = '''provided interface for «portName»'''
		provided.ifPresent [
			val dummyInherit = factory.createGeneralization
			dummyInherit.specific = dummyProvided
			dummyInherit.general = it
		]
		result.type = dummyProvided
		storePackaged(dummyProvided)
		required.ifPresent [
			val providedRequired = factory.createUsage
			providedRequired.clients += dummyProvided
			providedRequired.suppliers += it
			storePackaged(providedRequired)
		]
		result.name = portName
	}

}

class InPortExporter extends PortExporter {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isInPort(access)) factory.createPort
	}

	override exportContents(TypeDeclaration source) {
		val typeArguments = source.resolveBinding.superclass.typeArguments
		exportWithInterfaces(exportPortType(typeArguments.get(0)), Optional.empty,
			ElementTypeTeller.isBehavioralPort(source), source.name.identifier)
	}
}

class OutPortExporter extends PortExporter {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isOutPort(access)) factory.createPort
	}

	override exportContents(TypeDeclaration source) {
		val typeArguments = source.resolveBinding.superclass.typeArguments
		exportWithInterfaces(Optional.empty, exportPortType(typeArguments.get(0)),
			ElementTypeTeller.isBehavioralPort(source), source.name.identifier)
	}
}