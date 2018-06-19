package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.statement.ControlExporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import java.util.function.Consumer
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

	def assignToExpression(Expression modified,boolean isReplace, Function<Supplier<Action>, Action> assigned) {
		assignToExpression(modified, assigned, false, isReplace)
	}

	def assignToExpressionDelayed(Expression modified, Function<Supplier<Action>, Action> assigned) {
		assignToExpression(modified, assigned, true, true)
	}

	def assignToExpression(Expression modified, Function<Supplier<Action>, Action> assigned, boolean delayed, boolean isReplace) {
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

			delayWhen(delayed, [new SimpleFieldAccessExporter(this).createFieldAccess(base, field)]) [
				val rhs = assigned.apply([it])
				val write = factory.createAddStructuralFeatureValueAction
				write.isReplaceAll = isReplace
				write.structuralFeature = field
				storeNode(write)
				base.result.objectFlow(write.createObject("base", base.result.type))
				rhs.result.objectFlow(write.createValue("new_value", rhs.result.type))
				write.name = '''«base.name».«field.name»=«rhs.name»'''
				result.name = write.name
			]
		} else if (ElementTypeTeller.isVariable(modified)) {
			// operators that modify a variable
			val variableName = (modified as SimpleName).resolveBinding.name
			val variable = getVariable(variableName)
			delayWhen(delayed, [variable.read]) [
				val rhs = assigned.apply([it])
				val write = variable.write(rhs, isReplace)
				result.name = write.name
			]
		}
	}

	def void delayWhen(boolean delay, Supplier<Action> access, Consumer<Action> operation) {
		if (delay) {
			val temp = factory.createVariable
			storeVariable(temp)
			val act = access.get
			temp.name = "#temp"
			temp.type = act.result.type
			temp.write(act, true)
			operation.accept(access.get)
			temp.read
		} else {
			operation.accept(access.get)
			access.get
		}
	}

}