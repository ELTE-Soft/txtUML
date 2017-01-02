package hu.elte.txtuml.export.cpp.wizardz;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.fmu.EnvironmentExporter;
import hu.elte.txtuml.export.fmu.FMUConfig;
import hu.elte.txtuml.export.fmu.FMUExportGovernor;
import hu.elte.txtuml.export.fmu.FMUResourceHandler;
import hu.elte.txtuml.export.fmu.ModelDescriptionExporter;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;

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
			
			boolean generateFMU = createCppCodePage.generateFMU();
			String fmuDescription = createCppCodePage.getFMUDescription();

			TxtUMLToCppPage.PROJECT_NAME = txtUMLProject;
			TxtUMLToCppPage.MODEL_NAME = txtUMLModel;
			TxtUMLToCppPage.DESCRIPTION_NAME = threadManagmentDescription;

			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();

			TxtUMLToCppGovernor governor = new TxtUMLToCppGovernor(false);
			governor.uml2ToCpp(txtUMLProject, txtUMLModel, threadManagmentDescription, descriptionProjectName,
					addRuntimeOption, overWriteMainFileOption);
			
			if (generateFMU) {
				FMUExportGovernor fmuGovernor = new FMUExportGovernor();
				FMUConfig fmuConfig = fmuGovernor.extractFMUConfig(txtUMLProject, fmuDescription);
				ModelDescriptionExporter descriptionExporter = new ModelDescriptionExporter();
				EnvironmentExporter environmentExporter = new EnvironmentExporter();
				FMUResourceHandler resourceHandler = new FMUResourceHandler();
				IProject proj = ProjectUtils.getProject(txtUMLProject);
				Path genPath = Paths.get(proj.getLocation().toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME, txtUMLModel);
				descriptionExporter.export(Paths.get(proj.getLocation().toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME), fmuConfig);
				environmentExporter.export(genPath, fmuConfig);
				environmentExporter.exportHeader(genPath, fmuConfig);
				resourceHandler.copyResources(genPath);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
