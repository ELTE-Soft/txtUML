package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.LinkAction
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.LinkEndData

abstract class LinkActionExporterBase<T extends LinkAction> extends ActionExporter<MethodInvocation, T> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	protected def createEnd(LinkEndData end, ITypeBinding endType, Expression endValue) {
		end.end = fetchElement(endType) as Property
		if (endValue != null) {
			end.value = result.createInputValue("end_input", end.end.type)
			exportExpression(endValue).objectFlow(end.value)
		}	
		return end
	}
	
	protected def fetchAssocFromEnd(ITypeBinding endType) {
		fetchElement(endType.declaringClass) as Association
	}

}