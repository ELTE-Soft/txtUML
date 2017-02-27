package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.WizardUtils;
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
			IType threadManagementDescription = createCppCodePage.getThreadDescription();
			String descriptionProjectName = threadManagementDescription.getJavaProject().getElementName();

			TxtUMLToCppPage.setThreadManagerDescription(threadManagementDescription);

			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProject, txtUMLModel, threadManagmentDescription);
			if (!saveSucceeded)
				return false;

			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();

			Pair<String, String> model = WizardUtils.getModelByAnnotations(threadManagementDescription)
					.orElse(Pair.of("", ""));
			String txtUMLModel = model.getFirst();
			String txtUMLProject = model.getSecond();

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagementDescription.getFullyQualifiedName(),
					descriptionProjectName, addRuntimeOption, overWriteMainFileOption);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
