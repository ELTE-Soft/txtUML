package hu.elte.txtuml.validation.sequencediagram.visitors;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.ValidationErrors;

/**
 * Utils for sequence diagram validation.
 */
@SuppressWarnings("unchecked")
public class Utils {

	public static boolean isSequenceDiagramDescription(CompilationUnit unit) {
		List<AbstractTypeDeclaration> types = unit.types();
		return types.stream().anyMatch(
				td -> ElementTypeTeller.hasSuperClass(td.resolveBinding(), SequenceDiagram.class.getCanonicalName()));
	}

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
						collector.report(ValidationErrors.INVALID_POSITION.create(collector.getSourceInfo(), position));
					}

					// type of annotated field
					boolean isModelClass = ElementTypeTeller.hasSuperClass(elem.getType().resolveBinding(),
							ModelClass.class.getCanonicalName());
					if (!isModelClass) {
						collector.report(ValidationErrors.INVALID_LIFELINE_DECLARATION.create(collector.getSourceInfo(),
								elem.getType()));
					}
				}
			}
		}
	}

	public static void checkSendExists(ProblemCollector collector, Block node, ASTVisitor visitor) {
		List<Statement> statements = (List<Statement>) node.statements();
		boolean isLeaf = !statements.stream().anyMatch(stm -> stm instanceof WhileStatement
				|| stm instanceof IfStatement || stm instanceof ForStatement || stm instanceof DoStatement);
		if (!isLeaf) {
			return;
		}

		List<MethodInvocation> methodInvocations = statements.stream().map(stm -> ((Statement) stm))
				.filter(stm -> stm instanceof ExpressionStatement).map(stm -> (ExpressionStatement) stm)
				.map(ExpressionStatement::getExpression).filter(expr -> expr instanceof MethodInvocation)
				.map(expr -> (MethodInvocation) expr).collect(toList());

		List<MethodInvocation> notSequenceInvocations = methodInvocations.stream().filter(inv -> {
			try {
				String declaringClass = inv.resolveMethodBinding().getDeclaringClass().getQualifiedName();
				return !declaringClass.equals(Sequence.class.getCanonicalName())
						&& !declaringClass.equals(Action.class.getCanonicalName());
			} catch (NullPointerException ex) {
				return false;
			}
		}).collect(toList());

		boolean containsSend = methodInvocations.stream().anyMatch(Utils::isSendInvocation);
		if (!containsSend) {
			boolean isAllSequenceMethod = methodInvocations.stream().allMatch(inv -> {
				try {
					return inv.resolveMethodBinding().getDeclaringClass().getQualifiedName()
							.equals(Sequence.class.getCanonicalName());
				} catch (NullPointerException ex) {
					return false;
				}
			});

			if (methodInvocations.isEmpty() || isAllSequenceMethod) {
				collector.report(ValidationErrors.SEND_EXPECTED.create(collector.getSourceInfo(), node));
			}
			notSequenceInvocations.forEach(inv -> validateInvokedMethod(inv, visitor, collector));
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
			collector.report(ValidationErrors.INVALID_ACTION_CALL.create(collector.getSourceInfo(), elem));
		}
	}

	private static void validateInvokedMethod(MethodInvocation node, ASTVisitor visitor, ProblemCollector collector) {
		try {
			IMethodBinding binding = node.resolveMethodBinding();
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement()
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			if (unit == null) {
				return;
			}

			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());
			decl.getBody().accept(visitor);
		} catch (NullPointerException ex) {
			Logger.sys.debug("Method calling cannot be validated.");
		}
	}

	private static boolean isSendInvocation(MethodInvocation expression) {
		IMethodBinding mb = expression.resolveMethodBinding();
		return mb != null && (mb.getName().equals("send") || mb.getName().equals("fromActor"))
				&& mb.getDeclaringClass().getQualifiedName().equals(Sequence.class.getCanonicalName());
	}

}
