package hu.elte.txtuml.validation.sequencediagram.visitors;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

/**
 * Utils for sequence diagram validation.
 */
public class Utils {

	@SuppressWarnings("unchecked")
	public static List<AbstractTypeDeclaration> getSequenceDiagrams(CompilationUnit node) {
		List<AbstractTypeDeclaration> types = node.types();
		List<AbstractTypeDeclaration> sequenceDiagrams = types.stream()
				.map(td -> ((AbstractTypeDeclaration) td)).filter(td -> ElementTypeTeller
						.hasSuperClass(td.resolveBinding(), SequenceDiagram.class.getCanonicalName()))
				.collect(toList());
		return sequenceDiagrams;
	}

<<<<<<< HEAD
	public static void checkFieldDeclaration(ProblemCollector collector, FieldDeclaration elem) {
		List<?> modifiers = elem.modifiers();
		for (Object modifier : modifiers) {
			if (modifier instanceof Annotation) {
				Annotation ca = (Annotation) modifier;
				if (ca.getTypeName().getFullyQualifiedName().equals("Position")) {
					// position value
					SingleMemberAnnotation position;
					try {
						position = (SingleMemberAnnotation) ca;
					} catch (ClassCastException ex) {
						return;
					}
					int annotationVal = (int) position.getValue().resolveConstantExpressionValue();
					if (annotationVal < 0) {
						collector.report(
								SequenceErrors.INVALID_POSITION.create(collector.getSourceInfo(), position.getValue()));
					}

					// type of annotated field
					boolean isModelClass = ElementTypeTeller.hasSuperClass(elem.getType().resolveBinding(),
							ModelClass.class.getCanonicalName());
					if (!isModelClass) {
						collector.report(SequenceErrors.INVALID_LIFELINE_DECLARATION.create(collector.getSourceInfo(),
								elem.getType()));
					}
				}
			}
		}
	}

	public static void checkInvalidActionCall(MethodInvocation elem, ProblemCollector collector) {
		boolean isActionCall;
		try {
			isActionCall = elem.resolveMethodBinding().getDeclaringClass().getQualifiedName()
					.equals(Action.class.getCanonicalName());
		} catch (NullPointerException ex) {
			isActionCall = false;
		}
		if (isActionCall) {
			collector.report(SequenceErrors.INVALID_ACTION_CALL.create(collector.getSourceInfo(), elem));
		}
	}

	public static List<IfStatement> getIfNodes(List<Statement> statements) {
		List<IfStatement> ifNodes = statements.stream().filter(stm -> stm instanceof IfStatement)
				.map(stm -> (IfStatement) stm).collect(toList());
		return ifNodes;
	}

	public static boolean isSendInvocation(MethodInvocation expression) {
		IMethodBinding mb = expression.resolveMethodBinding();
		return mb != null && (mb.getName().equals("send") || mb.getName().equals("fromActor"))
				&& mb.getDeclaringClass().getQualifiedName().equals(Sequence.class.getCanonicalName());
	}

	public static boolean isLoopNode(Statement node) {
		return node instanceof WhileStatement || node instanceof ForStatement || node instanceof DoStatement;
	}
}
