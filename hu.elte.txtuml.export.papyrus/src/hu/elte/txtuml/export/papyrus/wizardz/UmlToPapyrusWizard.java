package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.MainAction;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard for visualizing Eclipse UML2 file from local filesystem
 *
 * @author András Dobreff
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
    MainAction ma = new MainAction(selectUmlPage.getProjectName(), getFileNameWithOutExtension(f), f.toURI().toString());
	ma.run();
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
 