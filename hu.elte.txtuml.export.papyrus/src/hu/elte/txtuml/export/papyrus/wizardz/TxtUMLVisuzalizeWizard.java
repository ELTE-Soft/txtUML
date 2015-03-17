package hu.elte.txtuml.export.papyrus.wizardz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.elte.txtuml.export.papyrus.MainAction;
import hu.elte.txtuml.export.papyrus.ProjectManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class TxtUMLVisuzalizeWizard extends Wizard{

	public static String LAUNCHCONFIG_NAME = "txtUML Visualization";
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
		//TODO Refactor this ugliness
		PreferencesManager preferncesManager = new PreferencesManager();
		final String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		final String txtUMLProjectName = selectTxtUmlPage.getTxtUmlProject();
		final String folder = preferncesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
		final String txtUMLExportProjectname = "hu.elte.txtuml.export.uml2"; // TODO Place in preferences
		String txtUMLExport =  txtUMLExportProjectname+".UML2";
		
		
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT, txtUMLProjectName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		
		try{
			final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			   ILaunchConfigurationType type =
			      manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
			ILaunchConfiguration[] configurations =
			      manager.getLaunchConfigurations(type);
			   for (int i = 0; i < configurations.length; i++) {
			      ILaunchConfiguration configuration = configurations[i];
			      if (configuration.getName().equals(LAUNCHCONFIG_NAME)) {
			         configuration.delete();
			         break;
			      }
			   }
			   
			ILaunchConfigurationWorkingCopy workingCopy =
			      type.newInstance(null, LAUNCHCONFIG_NAME);
		
			ArrayList array = new ArrayList();
			
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName);
			IJavaProject javaproject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
			
			IProject project2 = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLExportProjectname);
			IJavaProject javaproject2 = (IJavaProject) project2.getNature(JavaCore.NATURE_ID);
			
			IRuntimeClasspathEntry rcpEntry = JavaRuntime.newProjectRuntimeClasspathEntry(javaproject);
			IRuntimeClasspathEntry rcpEntry2 = JavaRuntime.newDefaultProjectClasspathEntry(javaproject2);
			IRuntimeClasspathEntry rcpEntry3 = JavaRuntime.newRuntimeContainerClasspathEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8"), IClasspathEntry.CPE_LIBRARY, javaproject2);
			
			array.add(rcpEntry.getMemento());
			array.add(rcpEntry2.getMemento());
			array.add(rcpEntry3.getMemento());
			
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, txtUMLModelName+"\n"+folder);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, txtUMLExport);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, txtUMLExportProjectname);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, array);
			
			
			ILaunchConfiguration configuration = workingCopy.doSave();
			
			List<String> list = (List<String>) configuration.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_BOOTPATH, Arrays.asList("NOTING"));
			list = (List<String>) configuration.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, Arrays.asList("NOTING"));
			
			DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
			
			manager.addLaunchListener(new ILaunchesListener2() {
				
				@Override
				public void launchesRemoved(ILaunch[] launches) {
				}
				
				@Override
				public void launchesChanged(ILaunch[] launches) {
				}
				
				@Override
				public void launchesAdded(ILaunch[] launches) {
				}
				
				@Override
				public void launchesTerminated(ILaunch[] launches){
					Display.getDefault().asyncExec(new Runnable() {
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
					});
					
					manager.removeLaunchListener(this);
				}
			});
			
		}catch(CoreException e){
			e.printStackTrace();
		}
		
		return true;
	}

}
