package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Property
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Vertex
import org.eclipse.uml2.uml.StateMachine
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.Region
import org.eclipse.uml2.uml.Transition

class ClassExporter extends Exporter<TypeDeclaration, ITypeBinding, Class> {

	private Region region

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(ITypeBinding typ) {
		if(ElementTypeTeller.isModelClass(typ)) factory.createClass
	}

	override exportContents(TypeDeclaration typeDecl) {
		val typeBnd = typeDecl.resolveBinding
		result.isAbstract = ElementTypeTeller.isAbstract(typeBnd)
		result.name = typeBnd.name
		val sm = result.createClassifierBehavior(result.name, UMLPackage.Literals.STATE_MACHINE) as StateMachine
		region = sm.createRegion(result.name)
		typeBnd.declaredFields.forEach[exportField]
		typeDecl.methods.forEach[exportOperation]
		typeDecl.types.forEach[exportElement(it, it.resolveBinding)]
	}

	override tryStore(Element contained) {
		switch contained {
			Operation: result.ownedOperations.add(contained)
			Property: result.ownedAttributes.add(contained)
			Vertex: region.subvertices.add(contained)
			Transition: region.transitions.add(contained)
			default: return false
		}
		return true
	}

}