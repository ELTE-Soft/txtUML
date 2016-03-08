package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.cpp.thread.ThreadDescriptionExporter;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2.ExportMode;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;
import hu.elte.txtuml.utils.eclipse.Dialogs;

class TxtUMLToCppGovernor {

	private static final String GeneratedCPPFolderName = "cpp-gen";
	private static final String UmlFilesFolderName = "model";

	private final boolean testing;

	TxtUMLToCppGovernor(boolean testing) {
		this.testing = testing;
	}

	@SuppressWarnings("unchecked")
	void uml2ToCpp(String txtUMLProject, String txtUMLModel, String deploymentDescription,
			boolean addRuntimeOption) throws Exception {
		String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation().toFile()
				.getAbsolutePath();
		String umlFilesFolder = txtUMLProject + File.separator + GeneratedCPPFolderName + File.separator + txtUMLModel
				+ File.separator + UmlFilesFolderName;

		Model model;
		try {
			model = TxtUMLToUML2.exportModel(txtUMLProject, txtUMLModel, umlFilesFolder, ExportMode.ExportActionCode);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error", e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
			throw e;
		}

		URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProject,
				ThreadDescriptionExporter.class.getClassLoader());
		Class<? extends Configuration> txtUMLThreadDescription;
		try {
			txtUMLThreadDescription = (Class<? extends Configuration>) loader.loadClass(deploymentDescription);
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

}
