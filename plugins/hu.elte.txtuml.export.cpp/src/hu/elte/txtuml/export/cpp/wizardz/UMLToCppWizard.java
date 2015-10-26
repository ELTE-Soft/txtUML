package hu.elte.txtuml.export.cpp.wizardz;

import java.io.File;
import java.util.regex.Pattern;

import hu.elte.txtuml.export.Uml2Utils;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.uml2.uml.Model;

public class UMLToCppWizard extends Wizard{
	
	private UMLToCppPage selectUMLPage;

	public UMLToCppWizard(){
		super();
		setNeedsProgressMonitor(true);
		
		
	}
	
	
	@Override
	public String getWindowTitle() {
		return "Generate C++ code from UML2 Model";
	}
	
	
	public void addPages(){
		selectUMLPage = new UMLToCppPage();
		addPage(selectUMLPage);
	}
	
	
	
	@Override
	public boolean performFinish() {
		
		
		String projectName = selectUMLPage.getProject();
		String UMLFilePath = selectUMLPage.getUMLFilePath();

		String pattern = Pattern.quote(File.separator);
		String splittedFileName[] = UMLFilePath.split(pattern);
		
		String umlFileFolder = "";
		for(int i = 0; i < splittedFileName.length - 1; i++ ){
			umlFileFolder = umlFileFolder  + splittedFileName[i] + File.separator;
		}
		
		String umlFileLocation = getLocationFromAPattern(splittedFileName,projectName);
		
		boolean runtimeOption = selectUMLPage.selectRuntimeOption();
		boolean debugOption = selectUMLPage.selectDebugOption();
		
		try{
			
			Model model = Uml2Utils.loadModel(URI.createPlatformResourceURI(projectName + File.separator + umlFileLocation, false));
			
			Uml2ToCppExporter cppExporter = new Uml2ToCppExporter(model,null,false,runtimeOption, debugOption);
			
			cppExporter.buildCppCode(umlFileFolder +  "cpp_codes");
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	private String getLocationFromAPattern(String[] splittedFileName,
			String pattern) {
		
		String retLocation = "";
		
		boolean located = false;
		for(String item: splittedFileName ){
			if(!located && item.equals(pattern)){
				located = true;
			}
			else if(located){
				retLocation = retLocation + File.separator + item;
			}
		}
		
		return retLocation;
		
	}

}
