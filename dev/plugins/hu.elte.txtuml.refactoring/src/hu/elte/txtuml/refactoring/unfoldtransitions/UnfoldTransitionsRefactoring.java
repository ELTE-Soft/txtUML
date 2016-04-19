package hu.elte.txtuml.refactoring.unfoldtransitions;

import static hu.elte.txtuml.refactoring.Type.COMPOSITE_STATE;
import static hu.elte.txtuml.refactoring.Type.STATE;
import static hu.elte.txtuml.refactoring.Type.TRANSITION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import hu.elte.txtuml.refactoring.BaseRefactoring;

public abstract class UnfoldTransitionsRefactoring extends BaseRefactoring {
	private IType transition;
	private IType composite;
	private String compositeName;

	protected abstract String getEndPointName();
	protected abstract hu.elte.txtuml.refactoring.Type getOccurrenceAnnotationType();
	protected abstract hu.elte.txtuml.refactoring.Type getOppositeAnnotationType();
	protected abstract String getNameOfNewTransition(IType transition, IType state) throws JavaModelException;
	
	@Override
	protected ICompilationUnit getTargetCompilationUnit() {
		return transition.getCompilationUnit();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();
		composite = null;
		if (!TRANSITION.get().equals(getSuperClassName(transition))) {
			status.addFatalError("The selected element is not a transition.");
			return status;
		}
		findFromAnnotation(status);
		if (composite == null) {
			status.addFatalError("The selected transition's " + getEndPointName() + " does not exist.");
			return status;
		}
		if (!COMPOSITE_STATE.get().equals(getSuperClassName(composite))) {
			status.addFatalError("The selected transition's " + getEndPointName() + " is not a composite state.");
		}
		if (!composite.hasChildren() || doesNotContainState(composite.getTypes())) {
			status.addFatalError("The selected transition's " + getEndPointName() + " does not contain states.");
		}
		return status;
	}

	private void findFromAnnotation(RefactoringStatus status) throws JavaModelException {
		for (IMemberValuePair pair : transition.getAnnotation(getOccurrenceAnnotationType().get()).getMemberValuePairs()) {
			if ("value".equals(pair.getMemberName())) {
				compositeName = (String) pair.getValue();
				Optional<IType> comp = findTypeByName(compositeName, transition);
				if (!comp.isPresent()) {
					status.addFatalError("The selected transition's " + getEndPointName() + " does not exist.");
				} else {
					composite = comp.get();
				}
			}
		}
	}

	private boolean doesNotContainState(IType[] types) {
		for (IType t : types) {
			if (STATE.get().equals(getSuperClassName(t))) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void rewriteDeclaringType(ASTRewrite astRewrite, CompilationUnit node) throws CoreException {
		AST ast = node.getAST();
		AbstractTypeDeclaration declaration = (AbstractTypeDeclaration) typeToDeclaration(transition.getDeclaringType(), node);
		ChildListPropertyDescriptor descriptor = typeToBodyDeclarationProperty(transition, node);
		AbstractTypeDeclaration oldTransition = (AbstractTypeDeclaration) typeToDeclaration(transition, node);

		for (IType state : getStatesOf(composite)) {
			TypeDeclaration newTransition = ast.newTypeDeclaration();
			setTransitionName(ast, state, newTransition);
			addAnnotations(ast, oldTransition, state, newTransition);
			newTransition.setSuperclassType(ast.newSimpleType(ast.newName(TRANSITION.get())));
			newTransition.bodyDeclarations().addAll(ASTNode.copySubtrees(ast, oldTransition.bodyDeclarations()));
			astRewrite.getListRewrite(declaration, descriptor).insertBefore(newTransition, oldTransition, null);
		}
		astRewrite.remove(oldTransition, null);
	}

	private void setTransitionName(AST ast, IType state, TypeDeclaration newTransition) throws JavaModelException {
		String transitionName = getNameOfNewTransition(transition, state);
		newTransition.setName(ast.newSimpleName(transitionName));
	}

	@SuppressWarnings("unchecked")
	private void addAnnotations(AST ast, AbstractTypeDeclaration oldTransition, IType state, TypeDeclaration newTransition) {
		for (Object obj : oldTransition.modifiers()) {
			SingleMemberAnnotation oldAnnotation = (SingleMemberAnnotation) obj;
			SingleMemberAnnotation newAnnotation = ast.newSingleMemberAnnotation();
			String oldTypeName = oldAnnotation.getTypeName().getFullyQualifiedName();
			newAnnotation.setTypeName(ast.newName(oldTypeName));
			createNewValueOrUseOld(ast, state, oldAnnotation, newAnnotation, oldTypeName);
			// annotations are stored in the modifiers list
			newTransition.modifiers().add(newAnnotation);
		}
	}

	private void createNewValueOrUseOld(AST ast, IType state, SingleMemberAnnotation oldAnnotation, SingleMemberAnnotation newAnnotation, String oldTypeName) {
		if (getOccurrenceAnnotationType().get().equals(oldTypeName)) {
			TypeLiteral newTypeLiteral = ast.newTypeLiteral();
			newTypeLiteral.setType(getQualifiedStateType(ast, state));
			newAnnotation.setValue(newTypeLiteral);
		} else {
			TypeLiteral oldTypeLiteral = (TypeLiteral) ASTNode.copySubtree(ast, oldAnnotation.getValue());
			newAnnotation.setValue(oldTypeLiteral);
		}
	}

	private QualifiedType getQualifiedStateType(AST ast, IType state) {
		return ast.newQualifiedType(
				getAncestors(ast),
				ast.newSimpleName(state.getElementName()));
	}
	
	private Type getAncestors(AST ast) {
		String[] split = compositeName.split("\\.");
		if (split.length == 1) {
			return ast.newSimpleType(ast.newSimpleName(split[0]));
		} else {
			Type t = ast.newQualifiedType(
					ast.newSimpleType(ast.newSimpleName(split[0])),
					ast.newSimpleName(split[1]));
			for (int i=1; i<split.length-1; ++i) {
				t = ast.newQualifiedType(t, ast.newSimpleName(split[i]));
			}
			return t;
		}
	}

	private List<IType> getStatesOf(IType composite) throws JavaModelException {
		List<IType> states = new ArrayList<>();
		for (IType t : composite.getTypes()) {
			if (COMPOSITE_STATE.get().equals(getSuperClassName(t)) || STATE.get().equals(getSuperClassName(t))) {
				states.add(t);
			}
		}
		return states;
	}

	public void setTransition(IType transition) {
		this.transition = transition;
	}
}