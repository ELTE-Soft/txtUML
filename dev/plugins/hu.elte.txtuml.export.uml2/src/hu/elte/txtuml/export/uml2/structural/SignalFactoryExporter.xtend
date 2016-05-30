package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Type

class SignalFactoryExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding access) {
		if(ElementTypeTeller.isSignal(access)) factory.createClass
	}

	override exportContents(TypeDeclaration source) {
		result.name = '''#«source.name.identifier»_factory'''
		val signalType = fetchElement(source.resolveBinding, new SignalExporter(this))
		source.methods.filter[isConstructor].map[exportOperation[result.ownedOperations += it]].forEach [
			addSignalParameter(signalType)
		]
		source.methods.filter[isConstructor].forEach[exportActivity[result.ownedBehaviors += it]]
		source.resolveBinding.declaredMethods.filter[isDefaultConstructor].forEach [
			val op = exportDefaultConstructor[result.ownedOperations += it]
			addSignalParameter(op, signalType)
			exportDefaultConstructorBody[result.ownedBehaviors += it]
		]
	}

	def addSignalParameter(Operation op, Type signalType) {
		op.isStatic = true
		val parameter = factory.createParameter
		parameter.type = signalType
		parameter.name = "signal"
		op.ownedParameters.add(0, parameter)
	}

}