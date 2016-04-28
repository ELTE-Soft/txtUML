package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.BinaryOperatorExporter
import hu.elte.txtuml.export.uml2.activity.expression.OperatorExporter
import java.util.function.Supplier
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.Assignment.Operator
import org.eclipse.uml2.uml.Action

class AssignExporter extends OperatorExporter<Assignment> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(Assignment access) { factory.createSequenceNode }
	
	override exportContents(Assignment source) {
		assignToExpression(source.leftHandSide)[ act |
			val right = exportExpression(source.rightHandSide)
			generateRhs(source.operator, act, right)
		]
	}

	def generateRhs(Operator op, Supplier<Action> leftVal, Action rhsVal) {
		val binops = new BinaryOperatorExporter(this)
		switch op {
			case Operator.ASSIGN:
				rhsVal
			case Operator.PLUS_ASSIGN:
				binops.exportOperator(binops.plusOp, "+", leftVal.get, rhsVal)
			case Operator.MINUS_ASSIGN:
				binops.exportOperator(binops.minusOp, "-", leftVal.get, rhsVal)
			case Operator.TIMES_ASSIGN:
				binops.exportOperator(binops.timesOp, "*", leftVal.get, rhsVal)
			case Operator.DIVIDE_ASSIGN:
				binops.exportOperator(binops.divideOp, "/", leftVal.get, rhsVal)
			case Operator.REMAINDER_ASSIGN:
				binops.exportOperator(binops.remainderOp, "%", leftVal.get, rhsVal)
			case Operator.BIT_AND_ASSIGN:
				binops.exportOperator(binops.andOp, "&", leftVal.get, rhsVal)
			case Operator.BIT_OR_ASSIGN:
				binops.exportOperator(binops.orOp, "|", leftVal.get, rhsVal)
		}
	}
}