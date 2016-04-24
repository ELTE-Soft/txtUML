package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import hu.elte.txtuml.project.wizards.pages.NewTxtUMLModelCreationPage;

public class TxtUMLModelCreatorWizard extends Wizard implements INewWizard {

	private IWorkbench workbench;
	private NewTxtUMLModelCreationPage _pageOne;

	public static final String TITLE = "New txtUML model";
	public static final String DESCRIPTION = "Create new txtUML Model";
	private IStructuredSelection selection;

	public TxtUMLModelCreatorWizard() {
		setWindowTitle(TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		if (!_pageOne.isPageComplete()) {
			return false;
		}

		boolean result = false;

		IFile file = _pageOne.createNewFile(); // _pageOne.createNewFile();
		result = file != null;

		if (result) {
			try {
				IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public void addPages() {
		super.addPages();

		_pageOne = new NewTxtUMLModelCreationPage();
		_pageOne.init(selection);

		addPage(_pageOne);
	}
}
