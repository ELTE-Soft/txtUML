package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import hu.elte.txtuml.project.wizards.pages.NewXtxtUMLFileWizardPage;

public class NewXtxtUMLFileCreationWizard extends Wizard implements INewWizard {

	public static final String TITLE = "New XtxtUML File";

	private IWorkbench workbench;
	private IStructuredSelection selection;
	private NewXtxtUMLFileWizardPage page;

	public NewXtxtUMLFileCreationWizard() {
		setWindowTitle(TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		if (!page.isPageComplete()) {
			return false;
		}

		IFile file = page.createNewFile();
		boolean result = file != null;

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

		page = new NewXtxtUMLFileWizardPage();
		page.init(selection);

		addPage(page);
	}

}
