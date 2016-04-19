package hu.elte.txtuml.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public abstract class BaseActionDelegate implements IWorkbenchWindowActionDelegate {
	protected IWorkbenchWindow window;
	protected List<IType> elements = new ArrayList<>();

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	protected void run(RefactoringWizard wizard, Shell parent, String dialogTitle) {
		try {
			RefactoringWizardOpenOperation operation = new RefactoringWizardOpenOperation(wizard);
			operation.run(parent, dialogTitle);
		} catch (InterruptedException exception) {
			// Do nothing
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		elements.clear();
		boolean error = false;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection extended = (IStructuredSelection) selection;
			Object[] selectionElements = extended.toArray();
			for (Object o : selectionElements) {
				if (o instanceof IType) {
					elements.add((IType) o);
				} else {
					action.setEnabled(false);
					error = true;
					break;
				}
			}
		}
		try {
			action.setEnabled(!error && !elements.isEmpty() && areRegular());
		} catch (JavaModelException exception) {
			action.setEnabled(false);
		}
	}

	protected boolean areRegular() throws JavaModelException {
		return elements.stream().allMatch(t -> {
			try {
				return isRegular(t);
			} catch (JavaModelException e) {
				return false;
			}
		});
	}
	
	protected boolean isRegular(IType type) throws JavaModelException {
		return type != null && type.exists() && type.isStructureKnown() && 
				type.getDeclaringType() != null
				&& !type.getDeclaringType().isAnnotation();
	}

	@Override
	public void dispose() {
	}
}
