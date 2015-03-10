package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.MainAction;
import hu.elte.txtuml.export.papyrus.ProjectManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
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
		final String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		final String folder = selectTxtUmlPage.getDestinationFolder();
		final String txtUMLProjectname = selectTxtUmlPage.getTxtUmlProjectName();
		String txtUMLExport =  "txtuml.export.uml2.ExportUML2";
		
		PreferencesManager preferncesManager = new PreferencesManager();
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		preferncesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER, folder);
		
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
		
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, txtUMLModelName+"\n"+folder);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, txtUMLExport);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, txtUMLProjectname);
			
			ILaunchConfiguration configuration = workingCopy.doSave();
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
					    	
					    	
					    	URI umlFileURI = URI.createFileURI(txtUMLProjectname+"/"+folder+"/"+txtUMLModelName+".uml");
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
