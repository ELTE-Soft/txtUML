package hu.elte.txtuml.xtxtuml.validation

import org.eclipse.xtext.xbase.util.XExpressionHelper;
import org.eclipse.xtext.xbase.XExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression

class XtxtUMLExpressionHelper extends XExpressionHelper {
	
	public override hasSideEffects(XExpression expr) {
		switch (expr) {
			RAlfDeleteObjectExpression,
			RAlfSendSignalExpression
				: true
			
			RAlfAssocNavExpression,
			RAlfSignalAccessExpression
				: false
				
			default
				: super.hasSideEffects(expr)
		}
	}
	
}
