package hu.elte.txtuml.export.uml2.restructured.activity.expression.assign

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.uml2.uml.SequenceNode

abstract class AssignExporter extends ActionExporter<Assignment, SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
}