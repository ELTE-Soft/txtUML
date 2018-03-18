package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.export.cpp.BuildSupport;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.SaveUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;

public class TxtUMLToCppWizard extends Wizard {

	private TxtUMLToCppPage createCppCodePage;
	private TxtUMLToCppOptionsPage createCppOptionsPage;

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
		createCppOptionsPage = new TxtUMLToCppOptionsPage();
		addPage(createCppCodePage);
		addPage(createCppOptionsPage);
	}

	@Override
	public boolean performFinish() {
		try {
			IType threadManagementDescription = createCppCodePage.getThreadDescription();
			String descriptionProjectName = threadManagementDescription.getJavaProject().getElementName();

			TxtUMLToCppPage.setThreadManagerDescription(threadManagementDescription);

			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();
			List<String> buildEnvironments = createCppOptionsPage.getSelectedBuildEnvironments();

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

			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProject, txtUMLModel,
					threadManagementDescription.getFullyQualifiedName());
			if (!saveSucceeded)
				return false;

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagementDescription.getFullyQualifiedName(),
					descriptionProjectName, addRuntimeOption, overWriteMainFileOption, buildEnvironments);

			String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation()
					.toFile().getAbsolutePath();

			String outputDirectory = projectFolder + File.separator + Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME
					+ File.separator + txtUMLModel;

			if (buildEnvironments != null && buildEnvironments.size() > 0) {
				getContainer().run(true, true, new BuildSupport(outputDirectory, buildEnvironments));
			}

		} catch (Exception e) {
			Dialogs.errorMsgb("C++ code generation error", e.getMessage(), e);
			return false;
		}
		return true;
	}

}
