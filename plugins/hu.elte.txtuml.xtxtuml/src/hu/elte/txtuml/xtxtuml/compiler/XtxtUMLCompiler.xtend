package hu.elte.txtuml.xtxtuml.compiler

import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression

class XtxtUMLCompiler extends XbaseCompiler {
	
	override protected doInternalToJavaStatement(XExpression obj, ITreeAppendable builder, boolean isReferenced) {
		switch (obj) {
			RAlfSendSignalExpression,
			RAlfDeleteObjectExpression : obj.toJavaStatement(builder)
			
			default : super.doInternalToJavaStatement(obj, builder, isReferenced)
		}
	}
	
	def dispatch toJavaStatement(RAlfSendSignalExpression sendExpr, ITreeAppendable builder) {
		builder.newLine;
		builder.append(hu.elte.txtuml.api.model.Action)
		builder.append(".send(");
		sendExpr.target.internalToJavaExpression(builder);
		builder.append(", ");
		sendExpr.signal.internalToJavaExpression(builder);
		builder.append(");");
	}
	
	def dispatch toJavaStatement(RAlfDeleteObjectExpression deleteExpr, ITreeAppendable builder) {
		builder.newLine;
		builder.append(hu.elte.txtuml.api.model.Action);
		builder.append(".delete(")
		deleteExpr.object.internalToJavaExpression(builder);
		builder.append(");");
	}
	
}