package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.eclipseutils.Dialogs;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.DefaultPapyrusModelManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * Wizard for visualizing Eclipse UML2 file from local filesystem
 */
public class UmlToPapyrusWizard extends Wizard {

  private SelectUMLPage selectUmlPage;
  private PreferencesPage preferencesPage;

  /**
   * The Constructor
   */
  public UmlToPapyrusWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
   */
  @Override
  public String getWindowTitle() {
    return "Create papyrus model from UML file";
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages() {
    selectUmlPage = new SelectUMLPage();
    preferencesPage = new PreferencesPage();
    addPage(selectUmlPage);
    addPage(preferencesPage);
  }

  /**
   * Sets the preferences and starts the visualization process.
   */
  @Override
  public boolean performFinish() {
	preferencesPage.setPreferences();
	File f = new File(selectUmlPage.getUMLPath()); 
	
	
    PapyrusVisualizer pv = new PapyrusVisualizer(selectUmlPage.getProjectName(), getFileNameWithOutExtension(f), f.toURI().toString());
	pv.registerPayprusModelManager(DefaultPapyrusModelManager.class);
    IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
	try {
		progressService.runInUI(
				progressService,
			      new IRunnableWithProgress() {
			         @Override
					public void run(IProgressMonitor monitor) {
			        	try {
		        			pv.run(monitor);
						} catch (Exception e) {
							Dialogs.errorMsgb(
									"txtUML visualization Error",
									"Error occured during the visualization process.", e);
							monitor.done();
						}
			         }
			      },
			      ResourcesPlugin.getWorkspace().getRoot());
	} catch (InvocationTargetException | InterruptedException e) {
		return false;
	}
	
    return true;
  }
  
  	/**
	 * Gets the FileName without extension
	 * @param file - The File
	 * @return - Filname without extension
	 */
	private String getFileNameWithOutExtension(File file){
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}
}
 