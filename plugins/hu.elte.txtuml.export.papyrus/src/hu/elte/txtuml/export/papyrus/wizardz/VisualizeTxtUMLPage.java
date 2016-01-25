package hu.elte.txtuml.export.papyrus.wizardz;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.dialogs.PackageSelectionDialog;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.utils.eclipse.NotFoundException;
import hu.elte.txtuml.utils.eclipse.PackageUtils;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;

/**
 * The Page for txtUML visualization.
 *
 * @author Andr�s Dobreff
 */
@SuppressWarnings("restriction")
public class VisualizeTxtUMLPage extends WizardPage {
	
	private static final String browseButtonText = "Browse...";
	
	private Composite container;
	private Text txtUMLModel;
	//private List<Text> txtUMLLayout = new LinkedList<>();
	private Text txtUMLProject;
	private Button generateSMDs;
	private ScrolledComposite sc;
	
	private Map<Integer, Text> txtUMLLayout = new HashMap<>();
	
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
		
		GridLayout layout = new GridLayout(4,false);
	    container.setLayout(layout);
	    
	    
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("txtUML Project: ");
	    txtUMLProject = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLProject.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT));
	    Button browseProject = new Button(container, SWT.NONE);
	    browseProject.setText(browseButtonText);
	    browseProject.addSelectionListener(new  SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog   dialog =
						new ElementTreeSelectionDialog(
								container.getShell(),
							    new WorkbenchLabelProvider(),
							    new WorkbenchContentProvider(){
									@Override
									public Object[] getChildren(Object element) {
										if(element instanceof IWorkspaceRoot){
											return ((IWorkspaceRoot) element).getProjects();
										}
										return new Object[0];
									}
								});
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.setTitle("Project Selection");
				dialog.open();
				Object[] result = dialog.getResult();
				if(result != null && result.length > 0){
					txtUMLProject.setText(((IProject) result[0]).getName());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	    
	    
	
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("txtUML Model: ");
	    txtUMLModel = new Text(container, SWT.BORDER | SWT.SINGLE);
	    txtUMLModel.setText(PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL));
	    Button browseModel = new Button(container, SWT.NONE);
	    browseModel.setText(browseButtonText );
	    browseModel.addSelectionListener(new  SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				JavaSearchScope scope = new JavaSearchScope();
				try {
					IJavaProject javaProject = ProjectUtils
							.findJavaProject(txtUMLProject.getText());
					for (IPackageFragmentRoot root : PackageUtils
							.getPackageFragmentRoots(javaProject)) {
						scope.add(root);
					}
				} catch (JavaModelException | NotFoundException ex) {
				}
				PackageSelectionDialog dialog = new PackageSelectionDialog(
						getShell(), getContainer(),
						PackageSelectionDialog.F_HIDE_DEFAULT_PACKAGE
								| PackageSelectionDialog.F_REMOVE_DUPLICATES
								| PackageSelectionDialog.F_HIDE_EMPTY_INNER,
						scope);

				dialog.setTitle("Project Selection");
				dialog.open();
				Object[] result = dialog.getResult();
				if (result != null && result.length > 0) {
					txtUMLModel.setText(((IPackageFragment) result[0])
							.getElementName());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

	    
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
	    generateSMDs.setSelection(PreferencesManager.getBoolean(PreferencesManager.GENERATE_STATEMACHINES_AUTOMATICALLY));
	    
	    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 2,1);
	    txtUMLModel.setLayoutData(gd);
	    txtUMLProject.setLayoutData(gd);
	    txtUMLLayout.values().forEach( (text) -> text.setLayoutData(GridData.FILL_HORIZONTAL) );
	    generateSMDs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4,1));
	    addDiagrambtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4,1)); //adding multiple descriptions
	    
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
		Integer temp_index;
		try{
			temp_index = Collections.max(txtUMLLayout.keySet())+1;
		}catch(NoSuchElementException e){
			temp_index = 1;
		}
		
		final Integer index = temp_index;
		
		final Label label = new Label(container, SWT.NONE);
        label.setText("txtUML Diagram"+index+": ");
        Control[] children = container.getChildren();
        label.moveAbove(children[children.length-3]);
        container.layout(new Control[] {label});
        
        final Text textField = new Text(container, SWT.BORDER);
        textField.setText(text);
        textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        children = container.getChildren();
        textField.moveAbove(children[children.length-3]);
        container.layout(new Control[] {textField});
        
        final Button browseBtn = new Button(container, SWT.NONE);
        browseBtn.setText(browseButtonText);
        browseBtn.addSelectionListener(new SelectionListener() {
	    	
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredTypesSelectionDialog dialog=
						new FilteredTypesSelectionDialog(container.getShell(),false,
								PlatformUI.getWorkbench().getProgressService(),
								SearchEngine.createWorkspaceScope(),
								IJavaSearchConstants.CLASS_AND_INTERFACE);
				dialog.open();
				Object[] result = dialog.getResult();
				if(result != null && result.length > 0 && result[0] instanceof SourceType){
					SourceType item = (SourceType) result[0];
					textField.setText(item.getFullyQualifiedName());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
        children = container.getChildren();
        browseBtn.moveAbove(children[children.length-3]);
	    container.layout(new Control[] {browseBtn});
	    
        final Button deleteButton = new Button(container, SWT.CENTER);
        deleteButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				label.dispose();
				textField.dispose();
				browseBtn.dispose();
				deleteButton.dispose();
				container.layout();
				txtUMLLayout.remove(index);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
        deleteButton.setText("X");
        children = container.getChildren();
        deleteButton.moveAbove(children[children.length-3]);
        container.layout(new Control[] {deleteButton});
        
        txtUMLLayout.put(index, textField);
        sc.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
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
		for(Text t: txtUMLLayout.values()){
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
