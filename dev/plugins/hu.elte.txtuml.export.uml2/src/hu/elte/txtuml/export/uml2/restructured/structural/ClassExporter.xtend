package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Region
import org.eclipse.uml2.uml.StateMachine
import org.eclipse.uml2.uml.Transition
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Vertex

class ClassExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	private Region region

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isModelClass(typ)) factory.createClass
	}

	override exportContents(TypeDeclaration typeDecl) {
		val typeBnd = typeDecl.resolveBinding
		result.isAbstract = ElementTypeTeller.isAbstract(typeBnd)
		result.name = typeBnd.name
		typeBnd.declaredFields.forEach[exportField[result.ownedAttributes += it]]
		typeDecl.methods.forEach[exportOperation[result.ownedOperations += it]]
		typeBnd.declaredMethods.filter[isDefaultConstructor].forEach[
			exportDefaultConstructor[result.ownedOperations += it]
			exportDefaultConstructorBody[result.ownedBehaviors += it]
		]
		typeDecl.methods.forEach[exportActivity[result.ownedBehaviors += it]]
		if (!typeDecl.types.isEmpty) {
			val sm = result.createClassifierBehavior(result.name, UMLPackage.Literals.STATE_MACHINE) as StateMachine
			region = sm.createRegion(result.name)
			typeDecl.types.forEach[exportElement(it, it.resolveBinding, [storeSMElement])]
		}
	}
	
	def storeSMElement(Element contained) {
		switch contained {
			Vertex: region.subvertices.add(contained)
			Transition: region.transitions.add(contained)
		}
	}

}