package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UMLToCppPage extends WizardPage{
	
	  private Text projectName;
	  private Text UmlFile;
	  private Button browse;
	  private Composite composite;
	  
	  private Button runtime;
	  private Button debug;

	protected UMLToCppPage() {
		super("Cpp Creator From UML Page");
		setTitle("Cpp Creator From UML Page");
		setDescription("Give the project name, and browse UML file to creat C++ code!");
	}

	@Override
	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
	    GridLayout gridLayout = new GridLayout();
	    composite.setLayout(gridLayout);
	    gridLayout.numColumns = 3;
	    
	    Label projectLabel = new Label(composite, SWT.NONE);
	    projectLabel.setText("Project name: ");
	    
	    projectName = new Text(composite, SWT.BORDER | SWT.SINGLE);
	    projectName.setText("");
	    
	    Label blank = new Label(composite, SWT.NONE);
	    blank.setText("");
	    
	    Label umlLabel = new Label(composite, SWT.NONE);
	    umlLabel.setText("UML file: ");
	    
	    UmlFile = new Text(composite, SWT.BORDER | SWT.SINGLE);
	    UmlFile.setText("");
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    projectName.setLayoutData(gd);
	    UmlFile.setLayoutData(gd);
	    
	    browse = new Button(composite,SWT.NONE);
	    browse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
	    browse.setText("Browse...");
	    
	    final String[] FILTER_NAMES = {"UML (*.uml)"};
	    final String[] FILTER_EXTS = { "*.uml","*.*"};
	
	    
	    browse.addSelectionListener(new SelectionAdapter() {
		    @Override
			public void widgetSelected(SelectionEvent event) {
	
			    FileDialog dlg = new FileDialog(composite.getShell(), SWT.OPEN);
			    dlg.setFilterNames(FILTER_NAMES);
			    dlg.setFilterExtensions(FILTER_EXTS);
			    String fn = dlg.open();
			    if (fn != null) {
				    UmlFile.setText(fn);
			      
			     }
		     }
	     });
	    
	    
	    runtime = new Button(composite, SWT.CHECK);
		runtime.setText("Set runtime option");
		
	    debug = new Button(composite, SWT.CHECK);
	    debug.setText("Set debug option");
	    
	    setControl(composite);
	    setPageComplete(true);
		
	}
	
	
	public String getProject(){
		return projectName.getText();
	}
	
	public String getUMLFilePath(){
		return UmlFile.getText();
	}
	
	public boolean selectRuntimeOption(){
		return runtime.getSelection();
	}
	
	public boolean selectDebugOption(){
		return debug.getSelection();
	}
	
	

}
