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
	
	private TxtUMLToCppPage createCppCodePage;
	
	public TxtUMLToCppWizard(){
		super();
		setNeedsProgressMonitor(true);
		
	}
	
	@Override
	public String getWindowTitle() {
		return "Generate C++ code from txtUML Model";
	}
	
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
			
			boolean runtimeOption = createCppCodePage.selectRuntimeOption();
			boolean debugOption = createCppCodePage.selectDebugOption();
			
			
			
			
			String genFolder = txtUMLProject + File.separator +  "gen";
			String projectFolder = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProject).getLocation().toFile().getAbsolutePath();
			
			String splitModelName[] = txtUMLModel.split("\\.");
			String simpleModelName = splitModelName[splitModelName.length - 1];
			
			String umlFilesFolder = genFolder + File.separator + "uml_files" + File.separator + simpleModelName;
			String umlFileLocation = umlFilesFolder + File.separator + txtUMLModel + ".uml";
			
			String cppFilesFolder = "gen" + File.separator + "cpp_codes" + File.separator + simpleModelName;
						
			try{
				ExportUtils.exportTxtUMLModelToUML2(txtUMLProject, txtUMLModel,
						umlFilesFolder);
			}catch(Exception e){
				Dialogs.errorMsgb("txtUML export Error",
    					e.getClass() + ":"+ System.lineSeparator() + e.getMessage(), e);
			}
			
			
			Model model = Uml2Utils.loadModel(URI.createPlatformResourceURI(umlFileLocation, false));
			Uml2ToCppExporter cppExporter;
			if(threadManagmentDescription.isEmpty()){
				
				cppExporter = new Uml2ToCppExporter(model,null,false,runtimeOption,debugOption);
			}
			else{
				//load description class
				URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProject, ThreadDescriptionExporter.class.getClassLoader());
				Class<?> txtUMLThreadDescription = loader.loadClass(threadManagmentDescription);
				ThreadDescriptionExporter exporter= new ThreadDescriptionExporter();
				exporter.exportDescription((Class<? extends Configuration>) txtUMLThreadDescription);
				
				cppExporter = new Uml2ToCppExporter(model,exporter.getConfigMap(),true,runtimeOption,debugOption);
			}
			
			
			cppExporter.buildCppCode(projectFolder + File.separator +  cppFilesFolder);
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	
}

