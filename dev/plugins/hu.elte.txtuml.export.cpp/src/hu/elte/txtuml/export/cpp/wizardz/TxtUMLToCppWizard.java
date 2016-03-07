package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.UML2.ExportMode;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.export.ExportUtils;
import hu.elte.txtuml.export.Uml2Utils;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.cpp.thread.ThreadDescriptionExporter;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;
import hu.elte.txtuml.utils.eclipse.Dialogs;

public class TxtUMLToCppWizard extends Wizard {

	private static final String GeneratedCPPFolderName = "cpp-gen";
	private static final String UmlFilesFolderName = "model";

	private boolean testing = false;
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

			uml2ToCpp(txtUMLProject, txtUMLModel, threadManagmentDescription, addRuntimeOption);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	void uml2ToCpp(String txtUMLProject, String txtUMLModel, String threadManagmentDescription,
			boolean addRuntimeOption) throws Exception {
		String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation().toFile()
				.getAbsolutePath();
		String umlFilesFolder = txtUMLProject + File.separator + GeneratedCPPFolderName + File.separator + txtUMLModel
				+ File.separator + UmlFilesFolderName;
		String umlFileLocation = umlFilesFolder + File.separator + txtUMLModel + ".uml";

		try {
			ExportUtils.exportTxtUMLModelToUML2(txtUMLProject, txtUMLModel, umlFilesFolder,
					ExportMode.ExportActionCode);
		} catch (Exception e) {
			if (!testing) {
				Dialogs.errorMsgb("txtUML export Error", e.getClass() + ":" + System.lineSeparator() + e.getMessage(),
						e);
			}
			throw e;
		}

		Model model = Uml2Utils.loadModel(URI.createPlatformResourceURI(umlFileLocation, false));

		URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProject,
				ThreadDescriptionExporter.class.getClassLoader());
		Class<? extends Configuration> txtUMLThreadDescription;
		try {
			txtUMLThreadDescription = (Class<? extends Configuration>) loader.loadClass(threadManagmentDescription);
		} catch (ClassNotFoundException e) {
			if (!testing) {
				Dialogs.errorMsgb("Description Class Error",
						e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
			}
			throw e;
		}

		List<org.eclipse.uml2.uml.Class> classList = new ArrayList<org.eclipse.uml2.uml.Class>();
		Shared.getTypedElements(classList, model.getOwnedElements(), UMLPackage.Literals.CLASS);
		Set<String> allClass = new HashSet<String>();
		for (org.eclipse.uml2.uml.Class cls : classList) {
			allClass.add(cls.getName());
		}

		ThreadDescriptionExporter exporter = new ThreadDescriptionExporter(allClass);
		exporter.exportDescription(txtUMLThreadDescription);

		if (!exporter.warningListIsEmpty()) {
			StringBuilder warnings = new StringBuilder("");
			for (String warning : exporter.getWarnings()) {
				warnings.append(warning + "\n");
			}
			warnings.append("\nWould you like to continue the generation?\n ");
			if (testing || !Dialogs.WarningConfirm("Description export reported warnings", warnings.toString())) {
				throw new Exception("Export was cancelled by user because of warnings");
			}
		}

		Uml2ToCppExporter cppExporter = new Uml2ToCppExporter(model, exporter.getConfigMap(), addRuntimeOption);
		try {
			cppExporter.buildCppCode(
					projectFolder + File.separator + GeneratedCPPFolderName + File.separator + txtUMLModel);
		} catch (Exception e) {
			if (!testing) {
				Dialogs.errorMsgb("Compilation failed", e.getClass() + ":" + System.lineSeparator() + e.getMessage(),
						e);
			}
			throw e;
		}
	}

	// for testing
	TxtUMLToCppWizard(boolean testing) {
		super();
		this.testing = true;
	}
}
