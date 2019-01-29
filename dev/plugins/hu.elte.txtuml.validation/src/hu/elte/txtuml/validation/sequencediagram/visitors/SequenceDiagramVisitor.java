package hu.elte.txtuml.validation.sequencediagram.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.SequenceErrors;

/**
 * Visitor for sequence diagram description validation.
 */
@SuppressWarnings("unchecked")
public class SequenceDiagramVisitor extends ASTVisitor {

	private ProblemCollector collector;
	private ASTNode placeOfError;

	public SequenceDiagramVisitor(ProblemCollector collector) {
		this.collector = collector;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		List<AbstractTypeDeclaration> sequenceDiagrams = Utils.getSequenceDiagrams(node);
		sequenceDiagrams.forEach(diag -> diag.accept(this));
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration elem) {
		checkFieldDeclaration(elem);
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		if (elem.getName().getFullyQualifiedName().equals("run")) {
			checkSendInRun(elem);
		}
		return false;
	}

	private void checkSendInRun(MethodDeclaration node) {
		final boolean showErrorHere = placeOfError == null;
		if (showErrorHere) {
			placeOfError = node.getBody();
		}
		checkSendInBlock(node.getBody(), showErrorHere);
		if (showErrorHere) {
			placeOfError = null;
		}
	}

	private void checkSendInLoopNode(Statement loopNode) {
		Statement body;
		if (loopNode instanceof WhileStatement) {
			WhileStatement whileLoop = (WhileStatement) loopNode;
			body = whileLoop.getBody();
		} else if (loopNode instanceof ForStatement) {
			ForStatement forLoop = (ForStatement) loopNode;
			body = forLoop.getBody();
		} else {
			DoStatement doWhileLoop = (DoStatement) loopNode;
			body = doWhileLoop.getBody();
		}
		boolean showErrorHere = placeOfError == loopNode.getParent() || placeOfError == loopNode;
		if (showErrorHere) {
			placeOfError = body;
		}
		if (body instanceof Block) {
			checkSendInBlock((Block) body, showErrorHere);
		} else {
			checkSendInStatement(body);
		}
	}

	private void checkSendInIfNode(IfStatement ifNode) {
		boolean showErrorHere = placeOfError == ifNode.getParent() || placeOfError == ifNode;
		Statement thenStatement = ifNode.getThenStatement();
		if (showErrorHere) {
			placeOfError = thenStatement;
		}
		if (thenStatement instanceof Block) {
			checkSendInBlock((Block) thenStatement, showErrorHere);
		} else {
			checkSendInStatement(thenStatement);
		}
		Statement elseStatement = ifNode.getElseStatement();
		if (showErrorHere) {
			placeOfError = elseStatement;
		}
		if (elseStatement == null) {
			return;
		}
		if (elseStatement instanceof IfStatement) {
			checkSendInIfNode((IfStatement) elseStatement);
		} else if (elseStatement instanceof Block) {
			checkSendInBlock((Block) elseStatement, showErrorHere);
		} else {
			checkSendInStatement(elseStatement);
		}
	}

	private void checkSendInBlock(Block block, boolean showErrorHere) {
		List<Statement> statements = (List<Statement>) block.statements();
		List<Statement> loops = Utils.getLoopNodes(statements);
		loops.forEach(loop -> {
			if (showErrorHere) {
				placeOfError = block;
			}
			checkSendInLoopNode(loop);
		});
		List<IfStatement> ifNodes = Utils.getIfNodes(statements);
		ifNodes.forEach(ifNode -> {
			if (showErrorHere) {
				placeOfError = block;
			}
			checkSendInIfNode(ifNode);
		});
		List<MethodInvocation> parFragments = Utils.getParFragments(statements);
		parFragments.forEach(parFragment -> {
			if (showErrorHere) {
				placeOfError = block;
			}
			checkSendInPar(parFragment);
		});
		List<MethodInvocation> methodInvocations = Utils.getMethodInvocations(statements);
		final List<Boolean> containsSendOrFragment = new ArrayList<>();
		methodInvocations.forEach(methodInvocation -> {
			if (showErrorHere) {
				placeOfError = methodInvocation;
			}
			containsSendOrFragment.add(checkSendOrFragmentInMethodInvocation(methodInvocation));
		});
		List<SuperMethodInvocation> superMethodInvocations = Utils.getSuperMethodInvocations(statements);
		superMethodInvocations.forEach(invocation -> {
			if (showErrorHere) {
				placeOfError = invocation;
			}
			containsSendOrFragment.add(checkSendOrFragmentInSuperMethodInvocation(invocation));
		});
		if (showErrorHere) {
			placeOfError = block;
		}
		boolean isLeaf = loops.isEmpty() && ifNodes.isEmpty() && parFragments.isEmpty();
		if (isLeaf && !containsSendOrFragment.contains(true)) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
		}
	}

	private void checkSendInStatement(Statement statement) {
		if (Utils.isLoopNode(statement)) {
			checkSendInLoopNode(statement);
		} else if (statement instanceof IfStatement) {
			checkSendInIfNode((IfStatement) statement);
		} else if (Utils.isParInvocation(statement)) {
			checkSendInPar(Utils.getMethodInvocationFromStatement(statement));
		} else if (Utils.isMethodInvocation(statement)) {
			checkSendInMethodInvocation(Utils.getMethodInvocationFromStatement(statement));
		} else if (Utils.isSuperMethodInvocation(statement)) {
			checkSendInSuperMethodInvocation(Utils.getSuperMethodInvocationFromStatement(statement));
		} else {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
		}
	}

	private void checkSendInPar(MethodInvocation parNode) {
		final boolean showErrorHere = placeOfError == parNode.getParent().getParent()
				|| placeOfError == parNode.getParent();
		if (showErrorHere) {
			placeOfError = parNode.getParent();
		}
		if (parNode.arguments().size() == 0) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			return;
		}
		parNode.arguments().forEach(argument -> {
			if (showErrorHere) {
				placeOfError = (ASTNode) argument;
			}
			checkSendInParArgument((Expression) argument);
		});
	}

	private void checkSendInParArgument(Expression argument) {
		checkSendInExpression(argument);
	}

	private void checkSendInExpression(Expression expression) {
		if (expression instanceof ArrayAccess) {
			ArrayAccess arrayAccess = (ArrayAccess) expression;
			AbstractTypeDeclaration declaration = Utils.getTypeDeclaration(arrayAccess.resolveTypeBinding());
			if (declaration != null) {
				declaration.accept(this);
			} else {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			}
		} else if (expression instanceof ArrayCreation) {
			ArrayCreation arrayCreation = (ArrayCreation) expression;
			if (arrayCreation.getInitializer() == null) {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
				return;
			}
			ArrayInitializer arrayInitializer = arrayCreation.getInitializer();
			if (placeOfError == expression) {
				placeOfError = arrayInitializer;
			}
			checkSendInExpression(arrayInitializer);
		} else if (expression instanceof ArrayInitializer) {
			ArrayInitializer arrayInitializer = (ArrayInitializer) expression;
			if (arrayInitializer.expressions().size() == 0) {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
				return;
			}
			arrayInitializer.expressions().forEach(expr -> {
				if (placeOfError == arrayInitializer) {
					placeOfError = (ASTNode) expr;
				}
				checkSendInExpression((Expression) expr);
			});
		} else if (expression instanceof Assignment) {
			Assignment assignment = (Assignment) expression;
			if (placeOfError == expression) {
				placeOfError = assignment.getRightHandSide();
			}
			checkSendInExpression(assignment.getRightHandSide());
		} else if (expression instanceof CastExpression) {
			CastExpression castExpression = (CastExpression) expression;
			if (placeOfError == expression) {
				placeOfError = castExpression.getExpression();
			}
			checkSendInExpression(castExpression.getExpression());
		} else if (expression instanceof ClassInstanceCreation) {
			ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
			if (!Utils.implementsInteraction(classInstanceCreation.resolveTypeBinding())) {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
				return;
			}
			AnonymousClassDeclaration anonymClass = classInstanceCreation.getAnonymousClassDeclaration();
			if (anonymClass != null) {
				if (placeOfError == expression) {
					placeOfError = null;
				}
				if (anonymClass != null) {
					anonymClass.accept(this);
				}
			} else {
				AbstractTypeDeclaration typeDeclaration = Utils
						.getTypeDeclaration(classInstanceCreation.getType().resolveBinding());
				if (typeDeclaration != null) {
					typeDeclaration.accept(this);
				} else {
					collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
				}
			}
		} else if (expression instanceof ConditionalExpression) {
			ConditionalExpression conditionalExpr = (ConditionalExpression) expression;
			boolean showErrorHere = false;
			if (placeOfError == expression) {
				showErrorHere = true;
				placeOfError = conditionalExpr.getThenExpression();
			}
			checkSendInExpression(conditionalExpr.getThenExpression());
			if (showErrorHere) {
				placeOfError = conditionalExpr.getElseExpression();
			}
			checkSendInExpression(conditionalExpr.getThenExpression());
		} else if (expression instanceof LambdaExpression) {
			LambdaExpression lambdaExpression = (LambdaExpression) expression;
			ASTNode body = lambdaExpression.getBody();
			boolean showErrorHere = placeOfError == expression;
			if (showErrorHere) {
				placeOfError = body;
			}
			if (body instanceof Block) {
				Block block = (Block) body;
				checkSendInBlock(block, showErrorHere);
			} else if (body instanceof MethodInvocation) {
				MethodInvocation methodInvocation = (MethodInvocation) body;
				checkSendInMethodInvocation(methodInvocation);
			} else {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			}
		} else if (expression instanceof MethodInvocation) {
			MethodInvocation invocation = (MethodInvocation) expression;
			Type returnType = Utils.getReturnTypeFromInvocation(invocation);
			AbstractTypeDeclaration declaration = Utils.getTypeDeclaration(returnType.resolveBinding());
			if (declaration != null) {
				declaration.accept(this);
			} else {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			}
		} else if (expression instanceof Name) {
			Name name = (Name) expression;
			AbstractTypeDeclaration declaration = Utils.getTypeDeclaration(name.resolveTypeBinding());
			if (declaration != null) {
				declaration.accept(this);
			} else {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			}
		} else if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenThesizedExpr = (ParenthesizedExpression) expression;
			if (placeOfError == expression) {
				placeOfError = parenThesizedExpr.getExpression();
			}
			checkSendInExpression(parenThesizedExpr.getExpression());
		} else if (expression instanceof SuperMethodInvocation) {
			SuperMethodInvocation superMethodInv = (SuperMethodInvocation) expression;
			AbstractTypeDeclaration declaration = Utils.getTypeDeclaration(superMethodInv.resolveTypeBinding());
			if (declaration != null) {
				declaration.accept(this);
			} else {
				collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			}
		} else if (expression instanceof ThisExpression) {
			collector.report(SequenceErrors.CYCLE_DETECTED.create(collector.getSourceInfo(), placeOfError));
		} else {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
		}
	}

	private void checkSendInMethodInvocation(MethodInvocation methodInvocation) {
		if (Utils.isSendInvocation(methodInvocation)) {
			return;
		}
		Block body = Utils.getMethodBodyFromInvocation(methodInvocation);
		if (body == null) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			return;
		}
		checkSendInBlock(body, false);
	}
	

	private void checkSendInSuperMethodInvocation(SuperMethodInvocation methodInvocation) {
		Block body = Utils.getMethodBodyFromInvocation(methodInvocation);
		if (body == null) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			return;
		}
		checkSendInBlock(body, false);
	}

	private boolean checkSendOrFragmentInMethodInvocation(MethodInvocation methodInvocation) {
		if (Utils.isSendInvocation(methodInvocation)) {
			return true;
		}
		Block body = Utils.getMethodBodyFromInvocation(methodInvocation);
		if (body == null) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			return false;
		}
		return checkSendOrFragmentInBlock(body);
	}

	private boolean checkSendOrFragmentInSuperMethodInvocation(SuperMethodInvocation methodInvocation) {
		Block body = Utils.getMethodBodyFromInvocation(methodInvocation);
		if (body == null) {
			collector.report(SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError));
			return false;
		}
		return checkSendOrFragmentInBlock(body);
	}

	private boolean checkSendOrFragmentInBlock(Block block) {
		List<Statement> statements = (List<Statement>) block.statements();
		List<Statement> loops = Utils.getLoopNodes(statements);
		loops.forEach(loop -> {
			checkSendInLoopNode(loop);
		});
		List<IfStatement> ifNodes = Utils.getIfNodes(statements);
		ifNodes.forEach(ifNode -> {
			checkSendInIfNode(ifNode);
		});
		List<MethodInvocation> parFragments = Utils.getParFragments(statements);
		parFragments.forEach(parFragment -> {
			checkSendInPar(parFragment);
		});
		List<MethodInvocation> methodInvocations = Utils.getMethodInvocations(statements);
		final List<Boolean> containsSendOrFragment = new ArrayList<>();
		methodInvocations.forEach(methodInvocation -> {
			containsSendOrFragment.add(checkSendOrFragmentInMethodInvocation(methodInvocation));
		});
		boolean isLeaf = loops.isEmpty() && ifNodes.isEmpty() && parFragments.isEmpty();
		return !isLeaf || containsSendOrFragment.contains(true);
	}

	private void checkFieldDeclaration(FieldDeclaration elem) {
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

}
