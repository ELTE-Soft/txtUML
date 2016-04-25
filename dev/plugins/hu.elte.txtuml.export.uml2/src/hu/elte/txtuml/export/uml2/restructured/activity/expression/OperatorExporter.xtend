package hu.elte.txtuml.export.uml2.restructured.activity.expression

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ControlExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import java.util.function.Function
import java.util.function.Supplier
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.SequenceNode

abstract class OperatorExporter<S> extends ControlExporter<S, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	def assignToExpression(Expression modified, Function<Supplier<Action>, Action> assigned) {
		assignToExpression(modified, assigned, false)
	}
	
	def assignToExpressionDelayed(Expression modified, Function<Supplier<Action>, Action> assigned) {
		assignToExpression(modified, assigned, true)
	}

	def assignToExpression(Expression modified, Function<Supplier<Action>, Action> assigned, boolean delayed) {
		if (ElementTypeTeller.isFieldAccess(modified)) {
			// operators that modify a field
			val fieldName = switch modified {
				SimpleName: modified.resolveBinding
				QualifiedName: modified.resolveBinding
				FieldAccess: modified.resolveFieldBinding
			}
			val field = fetchElement(fieldName) as Property
			val base = switch modified {
				SimpleName:
					thisRef((fieldName as IVariableBinding).declaringClass.fetchType)
				QualifiedName:
					if (modified.qualifier.resolveBinding instanceof IVariableBinding)
						exportExpression(modified.qualifier)
				FieldAccess:
					exportExpression(modified.expression)
			}
			val write = factory.createAddStructuralFeatureValueAction
			write.isReplaceAll = true
			write.structuralFeature = field
			base.result.objectFlow(write.createObject("base", base.result.type))
			storeNode(write)
			if (delayed) {
				val oldval = new SimpleFieldAccessExporter(this).createFieldAccess(base, field)
				val act = assigned.apply([new SimpleFieldAccessExporter(this).createFieldAccess(base, field)])
				result.name = act.name
				act.result.objectFlow(write.createValue("new_value", field.type))
				return oldval
			} else {
				val act = assigned.apply([new SimpleFieldAccessExporter(this).createFieldAccess(base, field)])
				result.name = act.name
				act.result.objectFlow(write.createValue("new_value", field.type))
				return new SimpleFieldAccessExporter(this).createFieldAccess(base, field)
			}
		} else if (ElementTypeTeller.isVariable(modified)) {
			// operators that modify a variable
			val variableName = (modified as SimpleName).resolveBinding.name
			val variable = getVariable(variableName)
			val newValue = assigned.apply([variable.read])
			result.name = newValue.name
			if (delayed) {
				val oldval = variable.read
				variable.write(newValue)
				return oldval				
			} else {
				variable.write(newValue)
				return variable.read
			}
		}
	}

}