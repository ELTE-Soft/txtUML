package hu.elte.txtuml.export.uml2.restructured.activity.expression.assign

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.BinaryOperatorExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ControlExporter
import java.util.function.Supplier
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.Assignment.Operator
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.SequenceNode

abstract class AssignExporter extends ControlExporter<Assignment, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	def generateRhs(Operator op, Supplier<Action> leftVal, Action rhsVal) {
		val binops = new BinaryOperatorExporter(this)
		switch op {
			case Operator.ASSIGN:
				rhsVal
			case Operator.PLUS_ASSIGN:
				binops.exportOperator(binops.plusOp, leftVal.get, rhsVal)
			case Operator.MINUS_ASSIGN:
				binops.exportOperator(binops.minusOp, leftVal.get, rhsVal)
			case Operator.TIMES_ASSIGN:
				binops.exportOperator(binops.timesOp, leftVal.get, rhsVal)
			case Operator.DIVIDE_ASSIGN:
				binops.exportOperator(binops.divideOp, leftVal.get, rhsVal)
			case Operator.REMAINDER_ASSIGN:
				binops.exportOperator(binops.remainderOp, leftVal.get, rhsVal)
			case Operator.BIT_AND_ASSIGN:
				binops.exportOperator(binops.andOp, leftVal.get, rhsVal)
			case Operator.BIT_OR_ASSIGN:
				binops.exportOperator(binops.orOp, leftVal.get, rhsVal)
		}
	}
}