package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.Wizard;
import hu.elte.txtuml.export.papyrus.MainAction;


public class UmlToPapyrusWizard extends Wizard {

  protected SelectUMLPage selectUmlPage;
  protected PreferencesPage preferencesPage;


  public UmlToPapyrusWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  @Override
  public String getWindowTitle() {
    return "Create papyrus model from UML file";
  }

  @Override
  public void addPages() {
    selectUmlPage = new SelectUMLPage();
    preferencesPage = new PreferencesPage();
    addPage(selectUmlPage);
    addPage(preferencesPage);
  }

  @Override
  public boolean performFinish() {
	preferencesPage.setPreferences();
	File f = new File(selectUmlPage.getUMLPath()); 
    MainAction ma = new MainAction(selectUmlPage.getProjectName(), getFileNameWithOutExtension(f), f.toURI().toString());
    IAction act = new Action() {};
	ma.run(act);
    return true;
  }
  
	private String getFileNameWithOutExtension(File file){
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}
}
 