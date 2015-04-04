package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class VisualizeTxtUMLPage extends WizardPage {
	private Composite container;
	private Text txtUMLModel;
	private Text txtUMLProject;
	private PreferencesManager preferencesManager;
	
	

	public VisualizeTxtUMLPage() {
		super("Visualize txtUML Page");
		setTitle("Visualize txtUML page");
	    setDescription("Give your txtUML project and model class to be visualized");
	    preferencesManager = new PreferencesManager();
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("txtUML Project: ");
	    txtUMLProject = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLProject.setText(preferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT));
	
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("txtUML Model: ");
	    txtUMLModel = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLModel.setText(preferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL));
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    txtUMLModel.setLayoutData(gd);
	    txtUMLProject.setLayoutData(gd);
	    
	    setControl(container);
	    setPageComplete(true);
	}

	public String getTxtUmlModelClass(){
		return txtUMLModel.getText();
	}
	
	public String getTxtUmlProject(){
		return txtUMLProject.getText();
	}
}
