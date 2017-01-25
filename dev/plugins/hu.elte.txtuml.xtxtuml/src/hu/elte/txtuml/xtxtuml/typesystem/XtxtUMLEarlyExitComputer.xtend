package hu.elte.txtuml.xtxtuml.typesystem;

import org.eclipse.xtext.xbase.XBasicForLoopExpression
import org.eclipse.xtext.xbase.XDoWhileExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XForLoopExpression
import org.eclipse.xtext.xbase.XWhileExpression
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer

class XtxtUMLEarlyExitComputer extends ExtendedEarlyExitComputer {

	/**
	 * Overrides the default implementation to delegate to child expressions
	 * even in case of loops.
	 */
	override isDefiniteEarlyExit(XExpression expr) {
		switch (expr) {
			XForLoopExpression: return isDefiniteEarlyExit(expr.eachExpression)
			XBasicForLoopExpression: return isDefiniteEarlyExit(expr.eachExpression)
			XWhileExpression: return isDefiniteEarlyExit(expr.body)
			XDoWhileExpression: return isDefiniteEarlyExit(expr.body)
			default: return super.isDefiniteEarlyExit(expr)
		}
	}

}
