package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.uml2.uml.ReadStructuralFeatureAction
import org.eclipse.uml2.uml.StructuralFeature
import org.eclipse.jdt.core.dom.Modifier

abstract class FieldAccessExporter<T> extends ActionExporter<T, ReadStructuralFeatureAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def IVariableBinding resolveBinding(T source)

	def Expression getExpression(T source)

	override exportContents(T source) {
		result.name = source.resolveBinding.name
		result.createResult(result.name, fetchType(source.resolveBinding.type))
		if (!Modifier.isStatic(source.resolveBinding.modifiers)) {
			val base = source.expression?.exportExpression ?:
				new ThisExporter(this).createThis(source.resolveBinding.type)
			base.result.objectFlow(result.createObject("object", base.result.type))
		}
		result.structuralFeature = fetchElement(source.resolveBinding) as StructuralFeature
	}
}

class NameFieldAccessExporter extends FieldAccessExporter<Name> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override resolveBinding(Name source) { source.resolveBinding as IVariableBinding }

	override getExpression(Name source) { if(source instanceof QualifiedName) (source as QualifiedName).qualifier }

	override create(Name access) {
		if(access.resolveBinding instanceof IVariableBinding &&
			(access.resolveBinding as IVariableBinding).isField) factory.createReadStructuralFeatureAction
	}
}

class SimpleFieldAccessExporter extends FieldAccessExporter<FieldAccess> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(FieldAccess access) { factory.createReadStructuralFeatureAction }

	override resolveBinding(FieldAccess source) { source.resolveFieldBinding }

	override getExpression(FieldAccess source) { source.expression }
}

