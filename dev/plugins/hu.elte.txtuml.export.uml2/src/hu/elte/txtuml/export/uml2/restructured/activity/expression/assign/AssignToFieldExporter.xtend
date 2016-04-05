package hu.elte.txtuml.export.uml2.restructured.activity.expression.assign

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.SimpleFieldAccessExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.ThisExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.UMLPackage

class AssignToFieldExporter extends AssignExporter {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(Assignment access) {
		if (ElementTypeTeller.isFieldAccess(access.leftHandSide)) factory.createSequenceNode
	}

	override exportContents(Assignment source) {
		val lhs = source.leftHandSide
		val fieldName = switch lhs {
			SimpleName: lhs.resolveBinding
			QualifiedName: lhs.resolveBinding
			FieldAccess: lhs.resolveFieldBinding
		}
		val base = switch lhs {
			SimpleName: new ThisExporter(this).createThis((fieldName as IVariableBinding).declaringClass)
			QualifiedName: if (lhs.qualifier.resolveBinding instanceof IVariableBinding) exportExpression(lhs.qualifier)
			FieldAccess: exportExpression(lhs.expression)
		}
		val field = fetchElement(fieldName) as Property
		val rhs = generateRhs(source.operator, [new SimpleFieldAccessExporter(this).createFieldAccess(base, field)], exportExpression(source.rightHandSide))
		val writeField = result.createNode('''«base?.name».«field.name»«source.operator»«rhs.name»''',
			UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION) as AddStructuralFeatureValueAction
		writeField.isReplaceAll = true
		writeField.structuralFeature = field
		result.name = writeField.name
		base.result.objectFlow(writeField.createObject("base", base.result.type))
		rhs.result.objectFlow(writeField.createValue("new_value", field.type))
		new SimpleFieldAccessExporter(this).createFieldAccess(base, field)
	}

}