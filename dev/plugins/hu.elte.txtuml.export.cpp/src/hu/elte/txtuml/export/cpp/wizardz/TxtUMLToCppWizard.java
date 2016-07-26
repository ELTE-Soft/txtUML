package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jface.wizard.Wizard;

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

			TxtUMLToCppPage.PROJECT_NAME = txtUMLProject;
			TxtUMLToCppPage.MODEL_NAME = txtUMLModel;
			TxtUMLToCppPage.DESCRIPTION_NAME = threadManagmentDescription;

			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagmentDescription, addRuntimeOption,
					overWriteMainFileOption);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
