package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.export.papyrus.ExportLauncher;
import hu.elte.txtuml.export.papyrus.MainAction;
import hu.elte.txtuml.export.papyrus.ProjectManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class TxtUMLVisuzalizeWizard extends Wizard{
	protected VisualizeTxtUMLPage selectTxtUMLPage;
	private VisualizeTxtUMLPage selectTxtUmlPage;
	
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
		final String txtUMLExportProjectname = "hu.elte.txtuml.export.uml2"; // TODO Place in preferences
		String txtUMLExport =  txtUMLExportProjectname+".UML2";
		
		try {
			Class<?> cls;

			
			/**/
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName);
			IJavaProject javaproject = JavaCore.create(project);
			
			String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(javaproject);
			
			List<URL> urlList = new ArrayList<URL>();
			for (int i = 0; i < classPathEntries.length; i++) {
			 String entry = classPathEntries[i];
			 IPath path = new Path(entry);
			 URL url = path.toFile().toURI().toURL();
			 urlList.add(url);
			}
			
			ClassLoader parentClassLoader = project.getClass().getClassLoader();
			URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
			URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
			
			
			cls = classLoader.loadClass(txtUMLLayoutName);
			classLoader.close();
			

			@SuppressWarnings("unchecked")
			Class<? extends Diagram> diagramClass = (Class<? extends Diagram>) cls;
			
			DiagramExporter exporter= DiagramExporter.create(diagramClass);
			
			DiagramExportationReport  report = exporter.export();
			System.out.println(report.getStatements());
			
			
		} catch (ClassNotFoundException | CoreException | IOException e1) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"Layout Diagram Error",e1.getMessage());
			e1.printStackTrace();
		}
		
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT, txtUMLProjectName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, txtUMLLayoutName);
	
		Runnable visualize = new Runnable() {
		    @Override
		    public void run() {
		    	
		    	
		    	URI umlFileURI = URI.createFileURI(txtUMLExportProjectname+"/"+folder+"/"+txtUMLModelName+".uml");
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
				IAction act = new Action() {};
				ma.run(act);
		    }
		};
		
		try{
			ExportLauncher launcher = new ExportLauncher();
			launcher.setExport(txtUMLExport);
			launcher.setExportProjectName(txtUMLExportProjectname);
			launcher.setFolderName(folder);
			launcher.setModelName(txtUMLModelName);
			launcher.setProjectName(txtUMLProjectName);
			launcher.setUpConfiguration();
			launcher.setAfterFinnish(visualize);
			launcher.run();
		}catch(CoreException e){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"Launch Configuration Error",e.getMessage());
			e.printStackTrace();
		}
		
		return true;
	}

}
