package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.SuperFieldAccess
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.ReadStructuralFeatureAction

abstract class FieldAccessExporter<T extends Expression> extends ActionExporter<T, ReadStructuralFeatureAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def IVariableBinding resolveBinding(T source)

	def Action getExpression(T source)

	override create(T access) {
		if(ElementTypeTeller.isFieldAccess(access)) factory.createReadStructuralFeatureAction
	}

	override exportContents(T source) {
		val base = source.expression
		val field = fetchElement(source.resolveBinding) as Property
		finishFieldAccess(result, base, field)
	}
	
	def createFieldAccess(Action base, Property field) {
		val ret = factory.createReadStructuralFeatureAction
		finishFieldAccess(ret, base, field)
		storeNode(ret)
		return ret
	}
	
	protected def finishFieldAccess(ReadStructuralFeatureAction action, Action base, Property field) {
		action.name = '''«base.name».«field.name»'''
		action.createResult(action.name, field.type)
		if (!field.isStatic) {
			base.result.objectFlow(action.createObject("base", base.result.type))
		}
		action.structuralFeature = field
	}
	
}

class NameFieldAccessExporter extends FieldAccessExporter<Name> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override resolveBinding(Name source) { source.resolveBinding as IVariableBinding }

	override getExpression(Name source) {
		if (source instanceof QualifiedName) {
			exportExpression((source as QualifiedName).qualifier)
		} else {
			thisRef((source.resolveBinding as IVariableBinding).declaringClass.fetchType)
		}
	}

}

class SimpleFieldAccessExporter extends FieldAccessExporter<FieldAccess> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override resolveBinding(FieldAccess source) { source.resolveFieldBinding }

	override getExpression(FieldAccess source) { exportExpression(source.expression) }
}

class SuperFieldAccessExporter extends FieldAccessExporter<SuperFieldAccess> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override resolveBinding(SuperFieldAccess source) { source.resolveFieldBinding }

	override getExpression(SuperFieldAccess source) {
		thisRef(source.resolveFieldBinding.declaringClass.fetchType)
	}
}

