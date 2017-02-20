package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.utils.eclipse.SaveUtils;


public class TxtUMLToCppWizard extends Wizard {

	private TxtUMLToCppPage createCppCodePage;

	public TxtUMLToCppWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Generate C++ code from txtUML Model";
	}

	@Override
	public void addPages() {
		createCppCodePage = new TxtUMLToCppPage();
		addPage(createCppCodePage);
	}

	@Override
	public boolean performFinish() {
		try {
			String txtUMLProject = createCppCodePage.getProject();
			String txtUMLModel = createCppCodePage.getModel();
			String threadManagmentDescription = createCppCodePage.getThreadDescription();
			String descriptionProjectName = createCppCodePage.getThreadDescriptionProjectName();

			TxtUMLToCppPage.PROJECT_NAME = txtUMLProject;
			TxtUMLToCppPage.MODEL_NAME = txtUMLModel;
			TxtUMLToCppPage.DESCRIPTION_NAME = threadManagmentDescription;
			TxtUMLToCppPage.DESCRIPTION_PROJECT_NAME = descriptionProjectName;

			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProject, txtUMLModel, threadManagmentDescription);
			if (!saveSucceeded)
				return false;
			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagmentDescription, descriptionProjectName,
					addRuntimeOption, overWriteMainFileOption);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
