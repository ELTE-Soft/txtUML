package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The Page for txtUML visualization.
 *
 * @author András Dobreff
 */
public class VisualizeTxtUMLPage extends WizardPage {
	private Composite container;
	private Text txtUMLModel;
	private Text txtUMLLayout;
	private Text txtUMLProject;
	private Button generateSMDs;
	
	/**
	 * The Constructor
	 */
	public VisualizeTxtUMLPage() {
		super("Visualize txtUML Page");
		setTitle("Visualize txtUML page");
	    setDescription("Give your txtUML project and model class to be visualized");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("txtUML Project: ");
	    txtUMLProject = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLProject.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT));
	
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("txtUML Model: ");
	    txtUMLModel = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLModel.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL));
	    
	    /** Future impovement - adding multiple descriptons **
	     *
	    Button addDiagrambtn = new Button(container, SWT.NONE);
	    addDiagrambtn.setText("Add txtUML diagram description");
	    addDiagrambtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Label label = new Label(container, SWT.NONE);
                label.setText("Name");
                Control[] children = container.getChildren();
                label.moveAbove(children[children.length-2]);
                container.layout(new Control[] {label});
                
                Text text = new Text(container, SWT.BORDER);
                GridData gd_text = new GridData(GridData.FILL_HORIZONTAL);
                text.setLayoutData(gd_text);
                children = container.getChildren();
                text.moveAbove(children[children.length-2]);
                container.layout(new Control[] {text});
                
                txtUMLLayout.add(text);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	    
	    /**
	    ** Future impovement - adding multiple descriptons **/
	    
	    /**/

	    Label label3 = new Label(container, SWT.NONE);
	    label3.setText("txtUML Layout Diagram: ");
	    Text diagram = new Text(container, SWT.BORDER | SWT.SINGLE);
	    diagram.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT));
	    txtUMLLayout = diagram;
	    
	    generateSMDs = new Button(container, SWT.CHECK);
	    generateSMDs.setText(" generate StateMachine Diagrams automatically");
	    //*/
	    
	    
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    txtUMLModel.setLayoutData(gd);
	    txtUMLProject.setLayoutData(gd);
	    txtUMLLayout.setLayoutData(gd);
	    generateSMDs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1));
	    //addDiagrambtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1)); //adding multiple descriptions
	    
	    setControl(container);
	    setPageComplete(true);
	}

	/**
	   * Returns the txtUML ModelClass
	   * @return Returns the txtUML ModelClass
	   */
	public String getTxtUmlModelClass(){
		return txtUMLModel.getText();
	}
	
	/**
	   * Returns the txtUML project's name
	   * @return Returns the txtUML project's name
	   */
	public String getTxtUmlProject(){
		return txtUMLProject.getText();
	}

	/**
	 * Returns the txtUML model layout
	 * @return
	 */
	public String getTxtUmlLayout() {
		return txtUMLLayout.getText();
	}
	
	/**
	 * Returns true if the user wants to generate StateMachines automatically
	 * @return
	 */
	public boolean getGenerateSMDs() {
		return generateSMDs.getSelection();
	}
}
