package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.DataType

class DataTypeExporter extends Exporter<TypeDeclaration, ITypeBinding, DataType> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isDataType(access)) factory.createDataType
	}

	override exportContents(TypeDeclaration source) {
		result.name = source.name.identifier
		source.resolveBinding.declaredFields.forEach[exportField[result.ownedAttributes += it]]
	}

}