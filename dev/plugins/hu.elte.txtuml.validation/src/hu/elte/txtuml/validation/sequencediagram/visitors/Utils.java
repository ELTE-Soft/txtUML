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
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.api.model.seqdiag.Sequence;

/**
 * Utils for sequence diagram validation.
 */
public class Utils {

	/*
	 * Returns the SequenceDiagrams in a CompilationUnit.
	 */
	@SuppressWarnings("unchecked")
	public static List<AbstractTypeDeclaration> getSequenceDiagrams(CompilationUnit node) {
		List<AbstractTypeDeclaration> types = node.types();
		List<AbstractTypeDeclaration> sequenceDiagrams = types.stream().map(td -> ((AbstractTypeDeclaration) td))
				.filter(td -> implementsInteraction(td.resolveBinding())).collect(toList());
		return sequenceDiagrams;
	}

	/*
	 * Returns the loops in the given list.
	 */
	public static List<Statement> getLoopNodes(List<Statement> statements) {
		List<Statement> loops = statements.stream().filter(Utils::isLoopNode).collect(toList());
		return loops;
	}

	/*
	 * Returns the IfStatements in the given list.
	 */
	public static List<IfStatement> getIfNodes(List<Statement> statements) {
		List<IfStatement> ifNodes = statements.stream().filter(stm -> stm instanceof IfStatement)
				.map(stm -> (IfStatement) stm).collect(toList());
		return ifNodes;
	}

	/*
	 * Returns the par fragments in the given list.
	 */
	public static List<MethodInvocation> getParFragments(List<Statement> statements) {
		List<MethodInvocation> parFragments = statements.stream().map(Utils::getMethodInvocationFromStatement)
				.filter(inv -> inv != null && inv.resolveMethodBinding() != null
						&& inv.resolveMethodBinding().getName().equals("par")
						&& isSequenceMethod(inv.resolveMethodBinding()))
				.collect(toList());
		return parFragments;
	}

	/*
	 * Returns true if the given MethodInvocation is a assertSend invocation.
	 */
	public static boolean isSendInvocation(MethodInvocation expression) {
		IMethodBinding mb = expression.resolveMethodBinding();
		return mb != null && (mb.getName().equals("assertSend") || mb.getName().equals("fromActor")) && isSequenceMethod(mb);
	}

	/*
	 * Returns true if the given statement is a loop.
	 */
	public static boolean isLoopNode(Statement node) {
		return node instanceof WhileStatement || node instanceof ForStatement || node instanceof DoStatement;
	}

	/*
	 * Returns the MethodInvocation from a statement.
	 */
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

	/*
	 * Returns the SuperMethodInvocation from a statement.
	 */
	public static SuperMethodInvocation getSuperMethodInvocationFromStatement(Statement statement) {
		if (statement instanceof ExpressionStatement) {
			Expression expression = ((ExpressionStatement) statement).getExpression();
			if (expression instanceof SuperMethodInvocation) {
				SuperMethodInvocation methodInvocation = (SuperMethodInvocation) expression;
				return methodInvocation;
			}
		}
		return null;
	}

	/*
	 * Returns true if the given statement is a par invocation.
	 */
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

	/*
	 * Returns the MethodInvocations in the given list
	 */
	public static List<MethodInvocation> getMethodInvocations(List<Statement> statements) {
		List<MethodInvocation> methodInvocations = statements.stream().map(Utils::getMethodInvocationFromStatement)
				.filter(inv -> inv != null && (isSendInvocation(inv) || !isSequenceMethod(inv.resolveMethodBinding())))
				.collect(toList());
		return methodInvocations;
	}

	/*
	 * Returns the SuperMethodInvocations in the given list
	 */
	public static List<SuperMethodInvocation> getSuperMethodInvocations(List<Statement> statements) {
		List<SuperMethodInvocation> superMethodInvocations = statements.stream()
				.map(Utils::getSuperMethodInvocationFromStatement).filter(inv -> inv != null).collect(toList());
		return superMethodInvocations;
	}

	/*
	 * Returns the body of the invoked Method.
	 */
	public static Block getMethodBodyFromInvocation(MethodInvocation methodInvocation) {
		IMethodBinding binding = methodInvocation.resolveMethodBinding();
		return getMethodBody(binding);
	}

	/*
	 * Returns the body of the invoked SuperMethod.
	 */
	public static Block getMethodBodyFromInvocation(SuperMethodInvocation methodInvocation) {
		IMethodBinding binding = methodInvocation.resolveMethodBinding();
		return getMethodBody(binding);
	}

	/*
	 * Returns the MethodBody from an IMethodBinding.
	 */
	private static Block getMethodBody(IMethodBinding binding) {
		try {
			CompilationUnit cu = getCompilationUnit(binding);
			MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());
			Block body = decl.getBody();
			return body;
		} catch (NullPointerException ex) {
			return null;
		}
	}

	/*
	 * Returns the return type of the invoked Method.
	 */
	public static Type getReturnTypeFromInvocation(MethodInvocation methodInvocation) {
		IMethodBinding binding = methodInvocation.resolveMethodBinding();
		try {
			CompilationUnit cu = getCompilationUnit(binding);
			MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());
			Type returnType = decl.getReturnType2();
			return returnType;
		} catch (NullPointerException ex) {
			return null;
		}
	}

	/*
	 * Returns true if the given type implements the Interaction interface.
	 */
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

	/*
	 * Returns the type declaration from an ITypeBinding.
	 */
	public static AbstractTypeDeclaration getTypeDeclaration(ITypeBinding binding) {
		try {
			CompilationUnit cu = getCompilationUnit(binding);
			AbstractTypeDeclaration declaration = (AbstractTypeDeclaration) cu.findDeclaringNode(binding.getKey());
			return declaration;
		} catch (NullPointerException ex) {
			return null;
		}
	}

	/*
	 * Returns true if the statement is a MethodInvocation.
	 */
	public static boolean isMethodInvocation(Statement statement) {
		if (statement instanceof ExpressionStatement) {
			Expression expression = ((ExpressionStatement) statement).getExpression();
			return expression instanceof MethodInvocation;
		}
		return false;
	}

	/*
	 * Returns true if the statement is a SuperMethodInvocation.
	 */
	public static boolean isSuperMethodInvocation(Statement statement) {
		if (statement instanceof ExpressionStatement) {
			Expression expression = ((ExpressionStatement) statement).getExpression();
			return expression instanceof SuperMethodInvocation;
		}
		return false;
	}

	/*
	 * Returns the CompilationUnit of an IBinding.
	 */
	private static CompilationUnit getCompilationUnit(IBinding binding) {
		try {
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement()
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			return cu;
		} catch (Exception ex) {
			return null;
		}
	}

	/*
	 * Returns true if the given IMethodBinding represents a Method from the
	 * Sequence class.
	 */
	private static boolean isSequenceMethod(IMethodBinding binding) {
		try {
			return binding.getDeclaringClass().getQualifiedName().equals(Sequence.class.getCanonicalName());
		} catch (NullPointerException ex) {
			return false;
		}
	}
}
