package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.Wizard;
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

public class TxtUMLToCppWizard extends Wizard {

    private static final String GenericFolderName = "cpp-gen";
    private static final String UmlFilesFolderName = "model";

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

    @SuppressWarnings("unchecked")
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

	    String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation()
		    .toFile().getAbsolutePath();

	    String umlFilesFolder = txtUMLProject + File.separator + GenericFolderName + File.separator + txtUMLModel
		    + File.separator + UmlFilesFolderName;

	    Model model;
	    try {
		model = TxtUMLToUML2.exportModel(txtUMLProject, txtUMLModel, umlFilesFolder,
			ExportMode.ExportActionCode);
	    } catch (Exception e) {
		e.printStackTrace();
		Dialogs.errorMsgb("txtUML export Error", e.getClass() + ":" + System.lineSeparator() + e.getMessage(),
			e);
		return false;
	    }

	    URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProject,
		    ThreadDescriptionExporter.class.getClassLoader());
	    Class<?> txtUMLThreadDescription;
	    try {
		txtUMLThreadDescription = loader.loadClass(threadManagmentDescription);
	    } catch (ClassNotFoundException e) {
		Dialogs.errorMsgb("Description Class Error",
			e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
		return false;
	    }

	    
	    Set<org.eclipse.uml2.uml.Class> allClass = new HashSet<org.eclipse.uml2.uml.Class>();
	    Shared.getTypedElements(allClass, model.getOwnedElements(), UMLPackage.Literals.CLASS);

	    ThreadDescriptionExporter exporter = new ThreadDescriptionExporter(allClass);
	    exporter.exportDescription((Class<? extends Configuration>) txtUMLThreadDescription);

	    if (!exporter.warningListIsEmpty()) {
		StringBuilder warnings = new StringBuilder("");
		for (String warning : exporter.getWarnings()) {
		    warnings.append(warning + "\n");
		}

		warnings.append("\nWould you like to continue the generation?\n ");
		if (!Dialogs.WarningConfirm("Description export warnings", warnings.toString())) {
		    return false;
		}
	    }

	    Uml2ToCppExporter cppExporter = new Uml2ToCppExporter(model, exporter.getConfigMap(), addRuntimeOption);
	    try {
		cppExporter.buildCppCode(
			projectFolder + File.separator + GenericFolderName + File.separator + txtUMLModel);

	    } catch (Exception e) {
		Dialogs.errorMsgb("C++ export error!", e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);

	    }

	} catch (Exception e) {

	    e.printStackTrace();
	    return false;
	}

	return true;

    }

}
