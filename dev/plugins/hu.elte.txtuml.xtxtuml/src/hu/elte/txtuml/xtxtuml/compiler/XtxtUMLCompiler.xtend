package hu.elte.txtuml.xtxtuml.compiler

import com.google.inject.Inject
import hu.elte.txtuml.api.model.Action
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XtxtUMLCompiler extends XbaseCompiler {

	@Inject extension IJvmModelAssociations;

	override protected doInternalToJavaStatement(XExpression obj, ITreeAppendable builder, boolean isReferenced) {
		switch (obj) {
			TUClassPropertyAccessExpression,
			RAlfDeleteObjectExpression,
			RAlfSendSignalExpression,
			RAlfSignalAccessExpression:
				obj.toJavaStatement(builder)
			default:
				super.doInternalToJavaStatement(obj, builder, isReferenced)
		}
	}

	def dispatch toJavaStatement(TUClassPropertyAccessExpression accessExpr, ITreeAppendable it) {
		// intentionally left empty
	}

	def dispatch toJavaStatement(RAlfSignalAccessExpression sigExpr, ITreeAppendable it) {
		// intentionally left empty
	}

	def dispatch toJavaStatement(RAlfDeleteObjectExpression deleteExpr, ITreeAppendable it) {
		newLine;
		append(Action);
		append(".delete(")
		deleteExpr.object.internalToJavaExpression(it);
		append(");");
	}

	def dispatch toJavaStatement(RAlfSendSignalExpression sendExpr, ITreeAppendable it) {
		newLine;
		append(Action)
		append(".send(");

		sendExpr.signal.internalToJavaExpression(it);
		append(", ");

		sendExpr.target.internalToJavaExpression(it);
		if (sendExpr.target.lightweightType.isSubtypeOf(Port)) {
			append(".required::reception");
		}

		append(");");
	}

	override protected internalToConvertedExpression(XExpression obj, ITreeAppendable it) {
		switch (obj) {
			TUClassPropertyAccessExpression,
			RAlfSignalAccessExpression:
				obj.toJavaExpression(it)
			default:
				super.internalToConvertedExpression(obj, it)
		}
	}

	def dispatch toJavaExpression(TUClassPropertyAccessExpression accessExpr, ITreeAppendable it) {
		accessExpr.left.internalToConvertedExpression(it);

		if (accessExpr.right instanceof TUAssociationEnd) {
			append(".assoc(");
		} else {
			append(".port(");
		}

		append(accessExpr.right.getPrimaryJvmElement as JvmType);
		append(".class)");
	}

	def dispatch toJavaExpression(RAlfSignalAccessExpression sigExpr, ITreeAppendable it) {
		append("getTrigger(");
		append(sigExpr.lightweightType);
		append(".class)");
	}

}
