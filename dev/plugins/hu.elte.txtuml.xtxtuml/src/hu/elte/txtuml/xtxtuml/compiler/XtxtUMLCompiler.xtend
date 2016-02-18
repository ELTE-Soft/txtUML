package hu.elte.txtuml.xtxtuml.compiler

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression

class XtxtUMLCompiler extends XbaseCompiler {
	
	@Inject extension IJvmModelAssociations;
	
	override protected doInternalToJavaStatement(XExpression obj, ITreeAppendable builder, boolean isReferenced) {
		switch (obj) {
			RAlfAssocNavExpression,
			RAlfDeleteObjectExpression,
			RAlfSendSignalExpression,
			RAlfSignalAccessExpression
				: obj.toJavaStatement(builder)
			
			default
				: super.doInternalToJavaStatement(obj, builder, isReferenced)
		}
	}
	
	def dispatch toJavaStatement(RAlfAssocNavExpression navExpr, ITreeAppendable it) {
		// intentionally left empty
	}
	
	def dispatch toJavaStatement(RAlfSignalAccessExpression sigExpr, ITreeAppendable it) {
		// intentionally left empty
	}
	
	def dispatch toJavaStatement(RAlfDeleteObjectExpression deleteExpr, ITreeAppendable it) {	
		newLine;
		append(hu.elte.txtuml.api.model.Action);
		append(".delete(")
		deleteExpr.object.internalToJavaExpression(it);
		append(");");
	}
	
	def dispatch toJavaStatement(RAlfSendSignalExpression sendExpr, ITreeAppendable it) {
		newLine;
		append(hu.elte.txtuml.api.model.Action)
		append(".send(");
		sendExpr.target.internalToJavaExpression(it);
		append(", ");
		sendExpr.signal.internalToJavaExpression(it);
		append(");");
	}
	
	override protected internalToConvertedExpression(XExpression obj, ITreeAppendable it) {
		switch (obj) {
			RAlfAssocNavExpression,
			RAlfSignalAccessExpression
				: obj.toJavaExpression(it)
			
			default
				: super.internalToConvertedExpression(obj, it)
		}
	}
	
	def dispatch toJavaExpression(RAlfAssocNavExpression navExpr, ITreeAppendable it) {
		navExpr.left.internalToConvertedExpression(it);
		append(".assoc(");
		append(navExpr.right.getPrimaryJvmElement as JvmType);
		append(".class)");
	}
	
	def dispatch toJavaExpression(RAlfSignalAccessExpression sigExpr, ITreeAppendable it) {
		append("((");
		append(sigExpr.lightweightType);
		append(") getSignal())");
	}

}
