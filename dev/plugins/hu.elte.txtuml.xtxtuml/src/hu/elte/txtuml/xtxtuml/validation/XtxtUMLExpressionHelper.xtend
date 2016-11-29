package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.util.XExpressionHelper

class XtxtUMLExpressionHelper extends XExpressionHelper {

	/**
	 * Extends the default behavior to XtxtUML expressions.
	 */
	public override hasSideEffects(XExpression expr) {
		switch (expr) {
			TUDeleteObjectExpression,
			TUSendSignalExpression:
				true
			TUSignalAccessExpression,
			TUClassPropertyAccessExpression:
				false
			default:
				super.hasSideEffects(expr)
		}
	}

}
