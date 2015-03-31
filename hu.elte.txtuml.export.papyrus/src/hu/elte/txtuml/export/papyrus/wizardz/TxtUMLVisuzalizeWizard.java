package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.MainAction;
import hu.elte.txtuml.export.papyrus.ProjectManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.uml2.UML2;
import hu.elte.txtuml.export.utils.ClassLoaderProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;

import java.io.IOException;
import java.net.URLClassLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Wizard for visualization of txtUML models 
 *
 * @author András Dobreff
 */
public class TxtUMLVisuzalizeWizard extends Wizard{
	protected VisualizeTxtUMLPage selectTxtUMLPage;
	private VisualizeTxtUMLPage selectTxtUmlPage;
	
	/**
	 * The Constructor
	 */
	public TxtUMLVisuzalizeWizard() {
		super();
	    setNeedsProgressMonitor(true);
	}
	

	@Override
	  public String getWindowTitle() {
	    return "Create Papyrus Model from txtUML Model";
	}
	
	@Override
	public void addPages() {
	  selectTxtUmlPage = new VisualizeTxtUMLPage();
	  addPage(selectTxtUmlPage);
	}
	
	@Override
	public boolean performFinish() {
		PreferencesManager preferncesManager = new PreferencesManager();
		final String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		final String txtUMLLayoutName = selectTxtUmlPage.getTxtUmlLayout();
		final String txtUMLProjectName = selectTxtUmlPage.getTxtUmlProject();		
		final String folder = preferncesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
		
		ClassLoader parentClassLoader = this.getClass().getClassLoader();

		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT, txtUMLProjectName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, txtUMLLayoutName);

		try(URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProjectName, parentClassLoader)){
			Class<?> txtUMLLayoutClass = loader.loadClass(txtUMLLayoutName); 
			@SuppressWarnings("unchecked")
			DiagramExporter exporter= DiagramExporter.create((Class<? extends Diagram>) txtUMLLayoutClass);
			DiagramExportationReport  report = exporter.export();
			System.out.println("Statements: "+report.getStatements());
		} catch (ClassNotFoundException | IOException e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"Layout Diagram Error",e.getMessage());
			e.printStackTrace();
		}

		try (URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(txtUMLProjectName, parentClassLoader)){
    		Class<?> txtUMLModelClass = loader.loadClass(txtUMLModelName);
    		String uri = URI.createPlatformResourceURI(txtUMLProjectName+"/"+folder, false).toString();
			UML2.exportModel(txtUMLModelClass, uri);
		} catch (Exception e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"txtUML export Error",e.getMessage());
			e.printStackTrace();
			return false;
		}

		try{
	    	URI umlFileURI = URI.createFileURI(txtUMLProjectName+"/"+folder+"/"+txtUMLModelName+".uml");
	    	URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
	    	IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(UmlFileResURI.toFileString()));
	    	
	    	URI diFileURI = URI.createFileURI(txtUMLModelName+"/"+txtUMLModelName+".di");
	    	URI diFileResURI = CommonPlugin.resolve(diFileURI);
	    	IFile diFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(diFileResURI.toFileString()));
	    	
	    	IEditorInput input = new FileEditorInput(diFile);

	    	IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(input);
	    	if(editor != null){
	    		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
	    	}

	    	ProjectManager projectManager = new ProjectManager();
			projectManager.deleteProjectbyName(txtUMLModelName);

	        MainAction ma  = new MainAction(txtUMLModelName, txtUMLModelName, UmlFile.getRawLocationURI().toString());
			ma.run();
    	}catch(Exception e){
    		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"txtUML visualization Error",e.getClass()+":\n"+e.getMessage());
			e.printStackTrace();
			return false;
    	}
		return true;
	}

}
