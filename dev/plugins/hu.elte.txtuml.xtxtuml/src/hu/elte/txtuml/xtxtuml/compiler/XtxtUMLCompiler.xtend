package hu.elte.txtuml.xtxtuml.compiler;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.Action
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.xtxtuml.common.XtxtUMLConnectiveHelper
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUBindExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectiveEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUCreateObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TULogExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStartObjectExpression
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.XConstructorCall
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XtxtUMLCompiler extends XbaseCompiler {

	@Inject extension IJvmModelAssociations;
	@Inject extension XtxtUMLConnectiveHelper;

	override protected doInternalToJavaStatement(XExpression obj, ITreeAppendable builder, boolean isReferenced) {
		switch (obj) {
			TUClassPropertyAccessExpression,
			TUStartObjectExpression,
			TUDeleteObjectExpression,
			TUBindExpression,
			TULogExpression,
			TUSendSignalExpression,
			TUSignalAccessExpression:
				obj.toJavaStatement(builder)
			default:
				super.doInternalToJavaStatement(obj, builder, isReferenced)
		}
	}

	def dispatch toJavaStatement(TUClassPropertyAccessExpression accessExpr, ITreeAppendable it) {
		// intentionally left empty
	}

	def dispatch toJavaStatement(TUSignalAccessExpression sigExpr, ITreeAppendable it) {
		// intentionally left empty
	}

	override def constructorCallToJavaExpression(XConstructorCall createExpr, ITreeAppendable it) {
		if(createExpr instanceof TUCreateObjectExpression){
				val withName = createExpr.objectName != null;
				if(createExpr.create || withName){
					append(Action);
					append('''.create«IF withName»WithName«ENDIF»(''');
					append('''«createExpr.constructor.simpleName».class«IF withName», "«createExpr.objectName»"«ENDIF»''');

					var i = 0;
					while(i < createExpr.arguments.size){
						append(", ");
						createExpr.arguments.get(i).internalToJavaExpression(it);
						i++;
					}
					append(")");
				}else{
					super.constructorCallToJavaExpression(createExpr, it);
				}
		}else{
			super.constructorCallToJavaExpression(createExpr, it);
		}
	}

	def dispatch toJavaStatement(TUStartObjectExpression startExpr, ITreeAppendable it) {
		newLine;
		append(Action);
		append(".start(");
		startExpr.object.internalToJavaExpression(it);
		append(");");
	}

	def dispatch toJavaStatement(TUDeleteObjectExpression deleteExpr, ITreeAppendable it) {
		newLine;
		append(Action);
		append(".delete(");
		deleteExpr.object.internalToJavaExpression(it);
		append(");");
	}

	def dispatch toJavaStatement(TULogExpression logExpr, ITreeAppendable it) {
		newLine;
		append(Action);
		append('''.log«IF logExpr.error»Error«ENDIF»(''');
		logExpr.log.internalToJavaExpression(it);
		append(");");
	}

	def dispatch toJavaStatement(TUBindExpression bindExpr, ITreeAppendable it) {
		newLine;
		append(Action);
		append('''.«bindExpr.type.toString»(''');

		val compileSide = [ TUConnectiveEnd end, XExpression participant |
			append(end.getPrimaryJvmElement as JvmType);
			append(".class, ");
			participant.internalToJavaExpression(it);
		];

		val inferredEnds = bindExpr.eAdapters.filter(XtxtUMLBindExpressionAdapter).head;
		if (bindExpr.connective.isDelegationConnector) {
			val sides = newLinkedList(inferredEnds.leftEnd -> bindExpr.leftParticipant,
				inferredEnds.rightEnd -> bindExpr.rightParticipant);
			if ((inferredEnds.rightEnd as TUConnectorEnd).role.container) {
				sides.reverse;
			}

			sides.head.value.internalToJavaExpression(it);
			append(", ");
			compileSide.apply(sides.last.key, sides.last.value);
		} else {
			compileSide.apply(inferredEnds.leftEnd, bindExpr.leftParticipant);
			append(", ");
			compileSide.apply(inferredEnds.rightEnd, bindExpr.rightParticipant);
		}

		append(");");
	}

	def dispatch toJavaStatement(TUSendSignalExpression sendExpr, ITreeAppendable it) {
		newLine;
		append(Action);
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
			TUSignalAccessExpression:
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

	def dispatch toJavaExpression(TUSignalAccessExpression sigExpr, ITreeAppendable it) {
		append("getTrigger(");
		append(sigExpr.lightweightType);
		append(".class)");
	}

}
