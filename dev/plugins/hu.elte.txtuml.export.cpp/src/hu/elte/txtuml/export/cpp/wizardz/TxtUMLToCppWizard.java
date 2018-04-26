package hu.elte.txtuml.export.cpp.wizardz;

import java.util.NoSuchElementException;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.EditorUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;

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

			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();

			Pair<String, String> model;
			try {
				model = WizardUtils.getModelByAnnotations(threadManagementDescription).get();
			} catch (NoSuchElementException e) {
				Dialogs.errorMsgb("C++ code generation error", "The model of the model classes cannot be determined.",
						e);
				return false;
			}
			String txtUMLModel = model.getFirst();
			String txtUMLProject = model.getSecond();

			boolean saveSucceeded = EditorUtils.saveAffectedFiles(getShell(), txtUMLProject, txtUMLModel,
					threadManagementDescription.getFullyQualifiedName());
			if (!saveSucceeded)
				return false;

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagementDescription.getFullyQualifiedName(),
					descriptionProjectName, addRuntimeOption, overWriteMainFileOption);
		} catch (Exception e) {
			Dialogs.errorMsgb("C++ code generation error", e.getMessage(), e);
			return false;
		}
		return true;
	}

}
