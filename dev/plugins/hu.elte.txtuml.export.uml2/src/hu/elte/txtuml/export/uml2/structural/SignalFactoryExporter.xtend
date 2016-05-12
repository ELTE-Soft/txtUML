package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class

class SignalFactoryExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isSignal(access)) factory.createClass
	}

	override exportContents(TypeDeclaration source) {
		result.name = '''#«source.name.identifier»_factory'''
		source.methods.filter[isConstructor].forEach[exportOperation[result.ownedOperations += it]]
		source.methods.filter[isConstructor].forEach[exportActivity[result.ownedBehaviors += it]]
		source.resolveBinding.declaredMethods.filter[isDefaultConstructor].forEach [
			exportDefaultConstructor[result.ownedOperations += it]
			exportDefaultConstructorBody[result.ownedBehaviors += it]
		]
	}

}