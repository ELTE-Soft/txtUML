package hu.elte.txtuml.refactoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;

public abstract class BaseRefactoring extends Refactoring {
	protected Map<ICompilationUnit, TextFileChange> fileChanges = new LinkedHashMap<>();

	protected abstract void rewriteDeclaringType(ASTRewrite astRewrite, CompilationUnit node) throws CoreException;

	protected abstract ICompilationUnit getTargetCompilationUnit();

	protected final String getSuperClassName(IType clazz) {
		try {
			return clazz.getSuperclassName();
		} catch (JavaModelException e) {
			return "";
		}
	}
	
	protected final Optional<IType> findTypeByName(String fullyQualifiedName, IType start) throws JavaModelException {
		IType container = start.getDeclaringType();
		String[] fullyQualifiedNameParts = fullyQualifiedName.split("\\.");
		for (int i=0; i<fullyQualifiedNameParts.length; ++i) {
			boolean found = false;
			for (IType t : container.getTypes()) {
				if (fullyQualifiedNameParts[i].equals(t.getElementName())) {
					container = t;
					found = true;
					break;
				}
			}
			if (!found) {
				return Optional.empty();
			}
		}
		return Optional.ofNullable(container);
	}

	protected final ChildListPropertyDescriptor typeToBodyDeclarationProperty(IType type, CompilationUnit node) throws JavaModelException {
		ASTNode result = typeToDeclaration(type, node);
		if (result instanceof AbstractTypeDeclaration) {
			return ((AbstractTypeDeclaration) result).getBodyDeclarationsProperty();
		} else if (result instanceof AnonymousClassDeclaration) {
			return AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY;
		}

		return null;
	}

	protected final ASTNode typeToDeclaration(IType type, CompilationUnit node) throws JavaModelException {
		Name result = (Name) NodeFinder.perform(node, type.getNameRange());
		if (type.isAnonymous()) {
			return getParent(result, AnonymousClassDeclaration.class);
		}
		return getParent(result, AbstractTypeDeclaration.class);
	}

	protected final <T> ASTNode getParent(ASTNode node, Class<T> parentClass) {
		do {
			node = node.getParent();
		} while (node != null && !parentClass.isInstance(node));
		return node;
	}

	protected void rewriteCompilationUnit(ICompilationUnit unit, ICompilationUnit target, CompilationUnit node) throws CoreException {
		ASTRewrite astRewrite = ASTRewrite.create(node.getAST());
		if (unit.equals(target)) {
			rewriteDeclaringType(astRewrite, node);
			rewriteAST(unit, astRewrite);
		}
	}

	protected final void rewriteAST(ICompilationUnit unit, ASTRewrite astRewrite) {
		try {
			MultiTextEdit edit = new MultiTextEdit();
			TextEdit astEdit = astRewrite.rewriteAST();
			edit.addChild(astEdit);
			TextFileChange changes = fileChanges.get(unit);
			if (changes == null) {
				changes = new TextFileChange(unit.getElementName(), (IFile) unit.getResource());
				changes.setTextType("java");
				changes.setEdit(edit);
			} else {
				changes.getEdit().addChild(edit);
			}
			fileChanges.put(unit, changes);
		} catch (MalformedTreeException exception) {
			RefactoringPlugin.log(exception);
		} catch (IllegalArgumentException exception) {
			RefactoringPlugin.log(exception);
		} catch (CoreException exception) {
			RefactoringPlugin.log(exception);
		}
	}

	@Override
	public Change createChange(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		try {
			monitor.beginTask("Creating change...", 1);
			final Collection<TextFileChange> changes = fileChanges.values();
			return new CompositeChange(getName(), changes.toArray(new Change[changes.size()]));
		} finally {
			monitor.done();
		}
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		final RefactoringStatus status = new RefactoringStatus();
		try {
			pm.beginTask("Checking preconditions...", 2);
			fileChanges.clear();
			final Map<ICompilationUnit, Collection<SearchMatch>> units = new HashMap<>();
			units.put(getTargetCompilationUnit(), new ArrayList<>());

			final Map<IJavaProject, Collection<ICompilationUnit>> projects = new HashMap<>();
			for (ICompilationUnit unit : units.keySet()) {
				IJavaProject project = unit.getJavaProject();
				if (project != null) {
					Collection<ICompilationUnit> collection = projects.get(project);
					if (collection == null) {
						collection = new ArrayList<>();
						projects.put(project, collection);
					}
					collection.add(unit);
				}
			}

			ASTRequestor requestors = new ASTRequestor() {
				@Override
				public void acceptAST(ICompilationUnit source, CompilationUnit ast) {
					try {
						rewriteCompilationUnit(source, getTargetCompilationUnit(), ast);
					} catch (CoreException exception) {
						RefactoringPlugin.log(exception);
					}
				}
			};

			IProgressMonitor subMonitor = new SubProgressMonitor(pm, 1);
			try {
				final Set<IJavaProject> set = projects.keySet();
				subMonitor.beginTask("Compiling source...", set.size());

				for (IJavaProject project : set) {
					ASTParser parser = ASTParser.newParser(AST.JLS8);
					parser.setProject(project);
					parser.setResolveBindings(true);
					Collection<ICompilationUnit> collection = projects.get(project);
					parser.createASTs(collection.toArray(new ICompilationUnit[collection.size()]), new String[0],requestors, new SubProgressMonitor(subMonitor, 1));
				}
			} finally {
				subMonitor.done();
			}
		} finally {
			pm.done();
		}
		return status;
	}
}
