package hu.elte.txtuml.refactoring.groupstates;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import hu.elte.txtuml.refactoring.BaseRefactoring;
import hu.elte.txtuml.refactoring.Type;

public class GroupStatesRefactoring extends BaseRefactoring {
	public static final String NAME = "Group States";
	private List<IType> selectedStates = new ArrayList<>();
	private String compositeStateName;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected ICompilationUnit getTargetCompilationUnit() {
		return selectedStates.get(0).getCompilationUnit();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();
		if (selectedStates.isEmpty()) {
			status.addFatalError("There are no states selected.");
		}
		if (!selectedStates.stream().allMatch(s -> Type.COMPOSITE_STATE.get().equals(getSuperClassName(s)) || Type.STATE.get().equals(getSuperClassName(s)))) {
			status.addFatalError("There is an element which is not state.");
		}
		IJavaElement parent = selectedStates.get(0).getParent();
		if (!selectedStates.stream().allMatch(s -> s.getParent().equals(parent))) {
			status.addFatalError("Selected states doesn't have the same parent.");
		}
		return status;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void rewriteDeclaringType(ASTRewrite astRewrite, CompilationUnit node) throws CoreException {
		AST ast = node.getAST();
		AbstractTypeDeclaration declaration = (AbstractTypeDeclaration) typeToDeclaration(selectedStates.get(0).getDeclaringType(), node);
		ChildListPropertyDescriptor descriptor = typeToBodyDeclarationProperty(selectedStates.get(0), node);
		
		TypeDeclaration newCompositeState = ast.newTypeDeclaration();
		newCompositeState.setName(ast.newSimpleName(compositeStateName));
		newCompositeState.setSuperclassType(ast.newSimpleType(ast.newName(Type.COMPOSITE_STATE.get())));
		for (IType state : selectedStates) {
			AbstractTypeDeclaration oldState = (AbstractTypeDeclaration) typeToDeclaration(state, node);
			newCompositeState.bodyDeclarations().add(ASTNode.copySubtree(ast, oldState));
			astRewrite.remove(oldState, null);
		}
		AbstractTypeDeclaration firstOldState = (AbstractTypeDeclaration) typeToDeclaration(selectedStates.get(0), node);
		astRewrite.getListRewrite(declaration, descriptor).insertAfter(newCompositeState, firstOldState, null);
	}

	public void setSelectedStates(List<IType> selectedStates) {
		this.selectedStates = selectedStates;
	}

	public void setCompositeStateName(String compositeStateName) {
		this.compositeStateName = compositeStateName;
	}
}