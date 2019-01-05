package hu.elte.txtuml.validation.sequencediagram.visitors;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
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

	public static List<Statement> getLoopNodes(List<Statement> statements) {
		List<Statement> loops = statements.stream().filter(Utils::isLoopNode).collect(toList());
		return loops;
	}

	public static List<IfStatement> getIfNodes(List<Statement> statements) {
		List<IfStatement> ifNodes = statements.stream().filter(stm -> stm instanceof IfStatement)
				.map(stm -> (IfStatement) stm).collect(toList());
		return ifNodes;
	}

	public static List<MethodInvocation> getParFragments(List<Statement> statements) {
		List<MethodInvocation> parFragments = statements.stream().map(Utils::getMethodInvocationFromStatement)
				.filter(inv -> inv != null && inv.resolveMethodBinding().getName().equals("par")
						&& inv.resolveMethodBinding().getDeclaringClass().getQualifiedName()
								.equals(Sequence.class.getCanonicalName()))
				.collect(toList());
		return parFragments;
	}

	public static boolean isSendInvocation(MethodInvocation expression) {
		IMethodBinding mb = expression.resolveMethodBinding();
		return mb != null && (mb.getName().equals("send") || mb.getName().equals("fromActor"))
				&& mb.getDeclaringClass().getQualifiedName().equals(Sequence.class.getCanonicalName());
	}

	public static boolean isLoopNode(Statement node) {
		return node instanceof WhileStatement || node instanceof ForStatement || node instanceof DoStatement;
	}

	public static MethodInvocation getMethodInvocationFromStatement(Statement statement) {
		if (statement instanceof ExpressionStatement) {
			Expression expression = ((ExpressionStatement) statement).getExpression();
			if (expression instanceof MethodInvocation) {
				MethodInvocation methodInvocation = (MethodInvocation) expression;
				return methodInvocation;
			}
		}
		return null;
	}

	public static boolean isParInvocation(Statement statement) {
		MethodInvocation parInvocation = getMethodInvocationFromStatement(statement);
		if (parInvocation != null) {
			IMethodBinding methodBinding = parInvocation.resolveMethodBinding();
			if (methodBinding == null) {
				return false;
			}
			return methodBinding.getName().equals("par")
					&& methodBinding.getDeclaringClass().getQualifiedName().equals(Sequence.class.getCanonicalName());
		}
		return false;
	}

	public static List<MethodInvocation> getMethodInvocations(List<Statement> statements) {
		List<MethodInvocation> methodInvocations = statements.stream().map(Utils::getMethodInvocationFromStatement)
				.filter(inv -> inv != null && !inv.resolveMethodBinding().getName().equals("par")).collect(toList());
		return methodInvocations;
	}

	public static Block getMethodBodyFromInvocation(MethodInvocation methodInvocation) {
		try {
			IMethodBinding binding = methodInvocation.resolveMethodBinding();
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement()
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());
			Block body = decl.getBody();
			return body;
		} catch (Exception ex) {
			return null;
		}
	}

	public static boolean implementsInteraction(ITypeBinding typeBinding) {
		if (typeBinding == null) {
			return false;
		}
		String interactionName = "hu.elte.txtuml.api.model.seqdiag.Interaction";
		if (typeBinding.getQualifiedName().equals(interactionName)) {
			return true;
		}
		for (ITypeBinding interfaceType : typeBinding.getInterfaces()) {
			if (implementsInteraction(interfaceType)) {
				return true;
			}
		}
		return implementsInteraction(typeBinding.getSuperclass());
	}

	public static AbstractTypeDeclaration getTypeDeclaration(Type type) {
		ITypeBinding binding = type.resolveBinding();
		try {
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement()
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			AbstractTypeDeclaration declaration = (AbstractTypeDeclaration) cu.findDeclaringNode(binding.getKey());
			return declaration;
		} catch (NullPointerException ex) {
			return null;
		}
	}
}
