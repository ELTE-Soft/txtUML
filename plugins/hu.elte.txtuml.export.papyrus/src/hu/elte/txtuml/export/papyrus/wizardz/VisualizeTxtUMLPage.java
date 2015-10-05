package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
	private List<Text> txtUMLLayout = new LinkedList<>();
	private Text txtUMLProject;
	private Button generateSMDs;
	private ScrolledComposite sc;
	
	private int LayoutCounter = 1;
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
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		container = new Composite(sc, SWT.NONE);
		
		GridLayout layout = new GridLayout();
	    layout.numColumns = 3;
	    container.setLayout(layout);
	    
	    
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("txtUML Project: ");
	    txtUMLProject = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLProject.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT));
	
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("txtUML Model: ");
	    txtUMLModel = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLModel.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL));
	    
	    /** Future improvement - adding multiple descriptons **
	     */
	    Button addDiagrambtn = new Button(container, SWT.NONE);
	    addDiagrambtn.setText("Add txtUML diagram description");
	    addDiagrambtn.addSelectionListener(new SelectionListener() {
	    	
			@Override
			public void widgetSelected(SelectionEvent e) {
				addLayoutField("");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	    
	    generateSMDs = new Button(container, SWT.CHECK);
	    generateSMDs.setText(" generate StateMachine Diagrams automatically");
	    generateSMDs.setSelection(true);
	    
	    GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 2,1);
	    txtUMLModel.setLayoutData(gd);
	    txtUMLProject.setLayoutData(gd);
	    txtUMLLayout.forEach( (text) -> text.setLayoutData(GridData.FILL_HORIZONTAL) );
	    generateSMDs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3,1));
	    addDiagrambtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3,1)); //adding multiple descriptions
	    
	    addInitialLayoutFields();
	    
	    sc.setContent(container);
	    sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    container.setSize(container.computeSize(400, 300, true));
	    sc.setMinSize(container.getSize());
	    sc.setSize(container.getSize());

	    setControl(parent);
	    setPageComplete(true);
	}
	
	private void addInitialLayoutFields(){
		Collection<String> layouts = PreferencesManager.getStrings(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT);
		for(String layout : layouts){
			if(!layout.isEmpty()) addLayoutField(layout);
		}
	}
	
	private void addLayoutField(String text){
		Label label = new Label(container, SWT.NONE);
        label.setText("txtUML Diagram"+LayoutCounter+": ");
        Control[] children = container.getChildren();
        label.moveAbove(children[children.length-3]);
        container.layout(new Control[] {label});
        
        Text textField = new Text(container, SWT.BORDER);
        textField.setText(text);
        textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        children = container.getChildren();
        textField.moveAbove(children[children.length-3]);
        container.layout(new Control[] {textField});
        
        Button deleteButton = new Button(container, SWT.NONE);
        deleteButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				label.dispose();
				textField.dispose();
				deleteButton.dispose();
				container.layout();
				txtUMLLayout.remove(textField);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
        deleteButton.setText("X");
        children = container.getChildren();
        deleteButton.moveAbove(children[children.length-3]);
        container.layout(new Control[] {deleteButton});
        
        txtUMLLayout.add(textField);
        sc.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        LayoutCounter++;
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
	public List<String> getTxtUmlLayout() {
		List<String> result = new LinkedList<String>();
		for(Text t: txtUMLLayout){
			if(!"".equals(t.getText())){
				result.add(t.getText());
			}
		}
		return result;
	}
	
	/**
	 * Returns true if the user wants to generate StateMachines automatically
	 * @return
	 */
	public boolean getGenerateSMDs() {
		return generateSMDs.getSelection();
	}
}
