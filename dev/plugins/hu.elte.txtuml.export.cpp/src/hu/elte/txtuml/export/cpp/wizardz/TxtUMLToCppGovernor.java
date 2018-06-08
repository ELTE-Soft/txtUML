package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.cpp.thread.ThreadDescriptionExporter;
import hu.elte.txtuml.export.uml2.ExportMode;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;
import hu.elte.txtuml.utils.eclipse.Dialogs;

class TxtUMLToCppGovernor {

	private static final String GeneratedCPPFolderName = Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME;
	private static final String UmlFilesFolderName = Uml2ToCppExporter.UML_FILES_FOLDER_NAME;

	private final boolean testing;

	TxtUMLToCppGovernor(boolean testing) {
		this.testing = testing;
	}

	void uml2ToCpp(String txtUMLProject, String txtUMLModel, String deploymentDescription,
			String deploymentDescriptionProjectName, boolean addRuntimeOption, boolean overWriteMainFileOption,
			List<String> buildEnvironments) throws Exception {

		String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation().toFile()
				.getAbsolutePath();
		String umlFilesFolder = txtUMLProject + File.separator + GeneratedCPPFolderName + File.separator + txtUMLModel
				+ File.separator + UmlFilesFolderName;

		Model model;
		try {
			model = TxtUMLToUML2.exportModel(txtUMLProject, txtUMLModel, umlFilesFolder,
					ExportMode.ExportActionsPedantic, GeneratedCPPFolderName);

		} catch (Exception e) {
			if (!testing) {
				Dialogs.errorMsgb("txtUML export Error", e.getClass() + ":" + System.lineSeparator() + e.getMessage(),
						e);
			}
			throw e;
		}

		Class<? extends Configuration> txtUMLThreadDescription;
		try {
			txtUMLThreadDescription = loadConfigurationClass(deploymentDescriptionProjectName, deploymentDescription);
		} catch (ClassNotFoundException | NotCofigurationClassException e) {
			if (!testing) {
				Dialogs.errorMsgb("Description Class Error",
						e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
			}
			throw e;
		}

		Set<org.eclipse.uml2.uml.Class> allClass = new HashSet<org.eclipse.uml2.uml.Class>();
		CppExporterUtils.getTypedElements(allClass, UMLPackage.Literals.CLASS, model.allOwnedElements());

		ThreadDescriptionExporter exporter = new ThreadDescriptionExporter(
				CppExporterUtils.getAllModelClassNames(model.allOwnedElements()));
		exporter.exportDescription((Class<? extends Configuration>) txtUMLThreadDescription);

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

		Uml2ToCppExporter cppExporter = new Uml2ToCppExporter(model.allOwnedElements(), exporter.getExportedConfiguration(),
				addRuntimeOption, overWriteMainFileOption, testing);

		try {
			cppExporter.buildCppCode(
					projectFolder + File.separator + GeneratedCPPFolderName + File.separator + txtUMLModel);

		} catch (Exception e) {
			if (!testing) {
				Dialogs.errorMsgb("C++ export error!", e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
			}
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Configuration> loadConfigurationClass(String deploymentDescriptionProjectName,
			String deploymentDescription) throws ClassNotFoundException, NotCofigurationClassException {
		URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(deploymentDescriptionProjectName,
				ThreadDescriptionExporter.class.getClassLoader());
		Class<? extends Configuration> txtUMLThreadDescription;
		txtUMLThreadDescription = (Class<? extends Configuration>) loader.loadClass(deploymentDescription);
		if (!txtUMLThreadDescription.getSuperclass().equals(Configuration.class)) {
			throw new NotCofigurationClassException("The selected deployment class is not a configuration class");

		}

		return txtUMLThreadDescription;
	}

}

@SuppressWarnings("serial")
class NotCofigurationClassException extends Exception {

	public NotCofigurationClassException() {
		super();
	}

	public NotCofigurationClassException(String message) {
		super(message);
	}

}
