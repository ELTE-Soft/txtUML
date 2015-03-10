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
	
	private Text destinationFolder;
	private Composite container;
	private Text txtUMLModel;
	private PreferencesManager preferencesManager;
	private Text txtUMLProjectName;

	public VisualizeTxtUMLPage() {
		super("Visualize txtUML Page");
		setTitle("Visualize txtUML page");
	    setDescription("Give the txtUML Model, the destination folder, and the txtUML Project name");
	    preferencesManager = new PreferencesManager();
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;

	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("txtUML Model: ");
	    txtUMLModel = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLModel.setText(preferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL));
	
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("Destination folder: ");
	    destinationFolder = new Text(container, SWT.BORDER | SWT.SINGLE);
	    destinationFolder.setText(preferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER));
	
	    Label label3 = new Label(container, SWT.NONE);
	    label3.setText("txtUML ProjectName: ");
	    txtUMLProjectName = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLProjectName.setText(preferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT));
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    txtUMLModel.setLayoutData(gd);
	    destinationFolder.setLayoutData(gd);
	    txtUMLProjectName.setLayoutData(gd);
	    
	    setControl(container);
	    setPageComplete(true);
	}

	public String getTxtUmlModelClass(){
		return txtUMLModel.getText();
	}
		  
	public String getDestinationFolder(){
		return destinationFolder.getText();
	}

	public String getTxtUmlProjectName() {
		return txtUMLProjectName.getText();
	}
}
