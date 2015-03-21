package hu.elte.txtuml.export.papyrus;

import java.util.ArrayList;

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
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


public class ExportLauncher {
	
	private final String LAUCHER_VM_TYPE = "org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8";
	private String LAUNCHCONFIG_NAME = "txtUML Visualization";
	private String txtUMLProjectName = "";
	private String txtUMLExportProjectname = "";
	private String txtUMLModelName = "";
	private String folder = "";
	private String txtUMLExport = "";
	private Runnable afterFinnish = new Runnable(){@Override public void run() {/*emptyMethod*/}}; 
	
	private ILaunchConfiguration configuration;
	private ILaunchManager manager;	

	/**
	 * Sets the projectName of the txtUML Model
	 * (Will be inserted into the ClassPaths)
	 * @author András Dobreff
	 */
	public void setProjectName(String projectName){
		this.txtUMLProjectName = projectName;
	}
	
	/**
	 * Sets the projectName of the txtUML export.uml library
	 * @author András Dobreff
	 */
	public void setExportProjectName(String projectName){
		this.txtUMLExportProjectname = projectName;
	}
	
	/**
	 * Sets the modelName of the txtUML Model
	 * (Will be added as program argument)
	 * @author András Dobreff
	 */
	public void setModelName(String modelName){
		this.txtUMLModelName = modelName;
	}
	
	/**
	 * Sets the the folder where the exorted uml shuld be saved.
	 * (Will be added as program argument)
	 * @author András Dobreff
	 */
	public void setFolderName(String folderName){
		this.folder = folderName;
	}
	
	/**
	 * Sets the ClassName of the txtUML export.uml library
	 * @author András Dobreff
	 */
	public void setExport(String className){
		this.txtUMLExport = className;
	}
	
	/**
	 * Sets a Runnable which will be executed after the Export finnished. 
	 * @author András Dobreff
	 */
	public void setAfterFinnish(Runnable afterFinnish){
		this.afterFinnish = afterFinnish;
	}
	
	public void run() throws CoreException{
		if(manager == null || configuration == null){
			setUpConfiguration();
		}
		
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
				Display.getDefault().asyncExec(afterFinnish);
				manager.removeLaunchListener(this);
			}
		});
	}
	
	/**
	 * Sets up the Run Configuration
	 * @author András Dobreff
	 * @throws CoreException 
	 */
	public void setUpConfiguration() throws CoreException{
		manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type =
		      manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
		for (int i = 0; i < configurations.length; i++) {
	      ILaunchConfiguration configuration = configurations[i];
	      if (configuration.getName().equals(LAUNCHCONFIG_NAME)) {
	         configuration.delete();
	         break;
	      }
		}
	   
		ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, LAUNCHCONFIG_NAME);
	
		ArrayList<String> array = new ArrayList<String>();
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName);
		IJavaProject javaproject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
		
		IProject project2 = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLExportProjectname);
		IJavaProject javaproject2 = (IJavaProject) project2.getNature(JavaCore.NATURE_ID);
		
		IRuntimeClasspathEntry rcpEntry = JavaRuntime.newProjectRuntimeClasspathEntry(javaproject);
		IRuntimeClasspathEntry rcpEntry2 = JavaRuntime.newDefaultProjectClasspathEntry(javaproject2);
		IRuntimeClasspathEntry rcpEntry3 = JavaRuntime.newRuntimeContainerClasspathEntry(new Path(LAUCHER_VM_TYPE), IClasspathEntry.CPE_LIBRARY, javaproject2);
		
		array.add(rcpEntry.getMemento());
		array.add(rcpEntry2.getMemento());
		array.add(rcpEntry3.getMemento());
		
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, txtUMLModelName+"\n"+folder);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, txtUMLExport);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, txtUMLExportProjectname);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, array);
		
		configuration = workingCopy.doSave();
	}
}
