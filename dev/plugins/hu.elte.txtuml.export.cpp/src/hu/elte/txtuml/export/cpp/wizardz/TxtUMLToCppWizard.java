package hu.elte.txtuml.export.cpp.wizardz;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.export.cpp.BuildSupport;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.EnvironmentNotFoundException;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.fmu.DebuggerExporter;
import hu.elte.txtuml.export.fmu.EnvironmentExporter;
import hu.elte.txtuml.export.fmu.FMUConfig;
import hu.elte.txtuml.export.fmu.FMUExportGovernor;
import hu.elte.txtuml.export.fmu.FMUResourceHandler;
import hu.elte.txtuml.export.fmu.FMUStandardCreator;
import hu.elte.txtuml.export.fmu.ModelDescriptionExporter;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;
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
		String outputDirectory = "";
		try {
			IType threadManagementDescription = createCppCodePage.getThreadDescription();
			String descriptionProjectName = threadManagementDescription.getJavaProject().getElementName();
			
			boolean generateFMU = createCppCodePage.generateFMU();
			String fmuDescription = createCppCodePage.getFMUDescription();
			
			TxtUMLToCppPage.FMU_NEEDED = generateFMU;
			TxtUMLToCppPage.FMU_CONFIG_FILE = fmuDescription;
			boolean addRuntimeOption = createCppCodePage.getAddRuntimeOptionSelection();
			boolean overWriteMainFileOption = createCppCodePage.getOverWriteMainFileSelection();
			List<String> buildEnvironments = createCppOptionsPage.getSelectedBuildEnvironments();
			String mainForOverride = createCppOptionsPage.getMainForOverride();

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
			
			if (generateFMU) {
				FMUExportGovernor fmuGovernor = new FMUExportGovernor();
				FMUConfig fmuConfig = fmuGovernor.extractFMUConfig(txtUMLProject, fmuDescription);
				ModelDescriptionExporter descriptionExporter = new ModelDescriptionExporter();
				EnvironmentExporter environmentExporter = new EnvironmentExporter();
				DebuggerExporter debuggerExporter = new DebuggerExporter();
				FMUResourceHandler resourceHandler = new FMUResourceHandler();
				IProject proj = ProjectUtils.getProject(txtUMLProject);
				Path genPath = Paths.get(proj.getLocation().toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME, txtUMLModel);
				descriptionExporter.export(Paths.get(proj.getLocation().toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME), fmuConfig);
				environmentExporter.export(genPath, fmuConfig);
				environmentExporter.exportHeader(genPath, fmuConfig);
				debuggerExporter.export(genPath, fmuConfig);
				resourceHandler.copyResources(genPath);
				
				FMUStandardCreator fmuCreator = new FMUStandardCreator();
				fmuCreator.buildFMUProject(genPath.toString());
			}
			
			String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation()
					.toFile().getAbsolutePath();

			outputDirectory = projectFolder + File.separator + Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME
					+ File.separator + txtUMLModel;

			if(mainForOverride != null && !mainForOverride.isEmpty()){
				File file = new File(mainForOverride);
				if(file.exists() && !file.isDirectory()){
					Files.copy(Paths.get(mainForOverride), Paths.get(outputDirectory + "/main.cpp"), StandardCopyOption.REPLACE_EXISTING);
				}
				else {
					throw new FileNotFoundException(mainForOverride + " file not found!");
				}
			}
			
			if (buildEnvironments != null && buildEnvironments.size() > 0) {
				BuildSupport buildSupport = new BuildSupport(outputDirectory, buildEnvironments);
				getContainer().run(true, true, buildSupport);
				buildSupport.handleErrors();
		
			}
			//TODO hotfix only, desktop not working on linux currently
			if(CppExporterUtils.isWindowsOS() && Desktop.isDesktopSupported()) { 
				Desktop.getDesktop().open(new File(outputDirectory));
			}
		} catch(FileNotFoundException e) {
			Dialogs.errorMsgb("C++ file copying error", "Unable to copy selected file", e);		
			return false;
		} catch (EnvironmentNotFoundException e) {		
			Dialogs.errorMsgb("C++ build environment generation error", "Not supported build environments selected", e);		
			return false;
		} catch (Exception e) {
			Dialogs.errorMsgb("C++ code generation error", e.getMessage(), e);
			return false;
		}
		return true;
	}

}
