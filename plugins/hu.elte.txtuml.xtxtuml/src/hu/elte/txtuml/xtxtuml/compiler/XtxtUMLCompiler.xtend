package hu.elte.txtuml.xtxtuml.compiler

import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XtxtUMLCompiler extends XbaseCompiler {
	
	@Inject extension IJvmModelAssociations;
	
	override protected doInternalToJavaStatement(XExpression obj, ITreeAppendable builder, boolean isReferenced) {
		switch (obj) {
			RAlfAssocNavExpression,
			RAlfDeleteObjectExpression,
			RAlfSendSignalExpression
				: obj.toJavaStatement(builder)
			
			default
				: super.doInternalToJavaStatement(obj, builder, isReferenced)
		}
	}
	
	def dispatch toJavaStatement(RAlfAssocNavExpression navExpr, ITreeAppendable builder) {
		navExpr.internalToJavaExpression(builder);
	}
	
	def dispatch toJavaStatement(RAlfDeleteObjectExpression deleteExpr, ITreeAppendable builder) {
		builder.newLine;
		builder.append(hu.elte.txtuml.api.model.Action);
		builder.append(".delete(")
		deleteExpr.object.internalToJavaExpression(builder);
		builder.append(");");
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
	
	override protected internalToConvertedExpression(XExpression obj, ITreeAppendable builder) {
		if (obj instanceof RAlfAssocNavExpression) {
			obj.toJavaExpression(builder);
		} else {
			super.internalToConvertedExpression(obj, builder);
		}
	}
	
	def toJavaExpression(RAlfAssocNavExpression navExpr, ITreeAppendable builder) {
		if (builder.hasName(navExpr)) {
			builder.append(builder.getName(navExpr));
			return;
		}
		
		declareFreshLocalVariable(navExpr, builder, [
			var expr = navExpr; // parameter is final

			while (expr.left instanceof RAlfAssocNavExpression) {
				expr = expr.left as RAlfAssocNavExpression;
			}
			
			expr.left.internalToJavaExpression(builder);
			
			while (expr != navExpr) {
				builder.append(".assoc(");
				builder.append(expr.right.getPrimaryJvmElement as JvmType)
				builder.append(".class).selectAny()");
				expr = expr.eContainer as RAlfAssocNavExpression;
			}
			
			builder.append(".assoc(");
			builder.append(expr.right.getPrimaryJvmElement as JvmType)
			builder.append(".class)");
		])
	}
	
}