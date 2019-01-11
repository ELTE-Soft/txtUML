package hu.elte.txtuml.validation.sequencediagram.visitors;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Proxy;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.SequenceErrors;

/**
 * Utils for sequence diagram validation.
 */
@SuppressWarnings("unchecked")
public class Utils {

	public static void acceptSequenceDiagrams(CompilationUnit node, ASTVisitor visitor) {
		List<AbstractTypeDeclaration> types = node.types();
		types.stream()
				.map(td -> ((AbstractTypeDeclaration) td)).filter(td -> ElementTypeTeller
						.hasSuperClass(td.resolveBinding(), SequenceDiagram.class.getCanonicalName()))
				.forEach(td -> td.accept(visitor));
	}

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
						collector.report(SequenceErrors.INVALID_POSITION.create(collector.getSourceInfo(), position));
					}

					// type of annotated field
					boolean isLifeline = ElementTypeTeller.hasSuperClass(elem.getType().resolveBinding(),
							Lifeline.class.getCanonicalName())
							|| ElementTypeTeller.hasSuperClass(elem.getType().resolveBinding(),
									Proxy.class.getCanonicalName());
					if (!isLifeline) {
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

}
