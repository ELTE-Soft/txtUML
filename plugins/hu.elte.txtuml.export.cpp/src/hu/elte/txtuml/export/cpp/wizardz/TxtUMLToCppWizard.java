package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;

import java.io.File;
import java.net.URLClassLoader;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Model;

import hu.elte.txtuml.eclipseutils.ClassLoaderProvider;
import hu.elte.txtuml.eclipseutils.Dialogs;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.export.cpp.description.ThreadDescriptionExporter;
import hu.elte.txtuml.export.cpp.description.Configuration;
import hu.elte.txtuml.export.ExportUtils;
import hu.elte.txtuml.export.Uml2Utils;


public class TxtUMLToCppWizard extends Wizard{
	
	private static final String GenericFolderName = "cpp_gen";
	private static final String UmlFilesFolderName = "uml_files";
	private static final String CppCodesFolderName = "cpp_codes";

	private TxtUMLToCppPage createCppCodePage;
	
	public TxtUMLToCppWizard(){
		super();
		setNeedsProgressMonitor(true);
		
	}
	
	@Override
	public String getWindowTitle() {
		return "Generate C++ code from txtUML Model";
	}
	
	@Override
	public void addPages(){
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
			
			boolean runtimeOption = createCppCodePage.getRuntimeOptionSelection();
			boolean debugOption = createCppCodePage.getDebugOptionSelection();
			
			String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation().toFile().getAbsolutePath();
			
			String umlFilesFolder = txtUMLProject + File.separator +  GenericFolderName + 
					File.separator + UmlFilesFolderName + File.separator + txtUMLModel;
			String umlFileLocation = umlFilesFolder + File.separator + txtUMLModel + ".uml";
						
			try{
				ExportUtils.exportTxtUMLModelToUML2(txtUMLProject, txtUMLModel,
						umlFilesFolder);
			}catch(Exception e){
				Dialogs.errorMsgb("txtUML export Error",
    					e.getClass() + ":"+ System.lineSeparator() + e.getMessage(), e);
			}
			
			
			Model model = Uml2Utils.loadModel(URI.createPlatformResourceURI(umlFileLocation, false));
			

			URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProject, ThreadDescriptionExporter.class.getClassLoader());
			Class<?> txtUMLThreadDescription = loader.loadClass(threadManagmentDescription);
			ThreadDescriptionExporter exporter= new ThreadDescriptionExporter();
			exporter.exportDescription((Class<? extends Configuration>) txtUMLThreadDescription);
			
			Uml2ToCppExporter cppExporter = new Uml2ToCppExporter(model,exporter.getConfigMap(),exporter.isMultiThreading(),runtimeOption,debugOption);
			
			cppExporter.buildCppCode(projectFolder + File.separator + 
					GenericFolderName + File.separator + CppCodesFolderName + File.separator + txtUMLModel);	
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	
}

