package hu.elte.txtuml.xd.validation

import hu.elte.txtuml.xd.xDiagramDefinition.PriorityInstruction
import org.eclipse.xtext.validation.Check

class XDiagramPriorityValidator extends XDiagramModelValidator {
	@Check
	def checkPhantomsInPriorityInstruction(PriorityInstruction priority){
		priority.^val.wrapped.expressions.filter[it.phantom != null].forEach[
			error("Inline anonymous phantoms are not supported in priority instruction.", it, null);
		]
	}
}