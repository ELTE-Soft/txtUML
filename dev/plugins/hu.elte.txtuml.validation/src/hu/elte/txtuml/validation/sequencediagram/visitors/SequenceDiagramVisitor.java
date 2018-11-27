package hu.elte.txtuml.validation.sequencediagram.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Statement;
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
			return true;
		}
		return false;
	}

	private void checkSendInRun(MethodDeclaration node) {
		final boolean showErrorHere = placeOfError == null;
		if (showErrorHere) {
			placeOfError = node.getBody();
		}
		List<Boolean> childChecks = new ArrayList<>();
		List<Statement> statements = (List<Statement>) node.getBody().statements();
		List<Statement> loops = Utils.getLoopNodes(statements);
		loops.forEach(loop -> {
			if (showErrorHere) {
				placeOfError = node.getBody();
			}
			childChecks.add(checkSendInLoopNode(loop));
		});
		List<IfStatement> ifNodes = Utils.getIfNodes(statements);
		ifNodes.forEach(ifNode -> {
			if (showErrorHere) {
				placeOfError = node.getBody();
			}
			childChecks.add(checkSendInIfNode(ifNode));
		});
		if (childChecks.isEmpty()) {
			SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError);
		}
	}

	private boolean checkSendInLoopNode(Statement loopNode) {
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
		if (placeOfError == loopNode.getParent() || placeOfError == loopNode) {
			placeOfError = body;
		}
		if (body instanceof Block) {
			return checkSendInBlock((Block) body);
		} else {
			return checkSendInStatement(body);
		}

	}

	private boolean checkSendInIfNode(IfStatement node) {
		boolean isValid = true;
		boolean showErrorHere = placeOfError == node.getParent() || placeOfError == node;
		Statement thenStatement = node.getThenStatement();
		if (showErrorHere) {
			placeOfError = thenStatement;
		}
		if (thenStatement instanceof Block) {
			isValid = isValid && checkSendInBlock((Block) thenStatement);
		} else {
			isValid = isValid && checkSendInStatement(thenStatement);
		}
		Statement elseStatement = node.getElseStatement();
		if (showErrorHere) {
			placeOfError = elseStatement;
		}
		if (elseStatement instanceof IfStatement) {
			isValid = isValid && checkSendInIfNode((IfStatement) elseStatement);
		} else if (elseStatement instanceof Block) {
			isValid = isValid && checkSendInBlock((Block) elseStatement);
		} else {
			isValid = isValid && checkSendInStatement(elseStatement);
		}
		return isValid;
	}

	private boolean checkSendInBlock(Block node) {
		final boolean showErrorHere = placeOfError == node;
		List<Statement> statements = (List<Statement>) node.statements();
		List<Statement> loops = Utils.getLoopNodes(statements);
		List<Boolean> childChecks = new ArrayList<>();
		loops.forEach(loop -> {
			if (showErrorHere) {
				placeOfError = node;
			}
			childChecks.add(checkSendInLoopNode(loop));
		});
		List<IfStatement> ifNodes = Utils.getIfNodes(statements);
		ifNodes.forEach(ifNode -> {
			if (showErrorHere) {
				placeOfError = node;
			}
			childChecks.add(checkSendInIfNode(ifNode));
		});
		if (childChecks.isEmpty()) {
			SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError);
			return false;
		}
		return !childChecks.contains(false);
	}

	private boolean checkSendInStatement(Statement node) {
		if (Utils.isLoopNode(node)) {
			return checkSendInLoopNode(node);
		} else if (node instanceof IfStatement) {
			return checkSendInIfNode((IfStatement) node);
		} else {
			SequenceErrors.SEND_EXPECTED.create(collector.getSourceInfo(), placeOfError);
			return false;
		}
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
