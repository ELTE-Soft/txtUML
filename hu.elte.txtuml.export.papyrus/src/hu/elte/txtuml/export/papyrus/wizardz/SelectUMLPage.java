package hu.elte.txtuml.export.papyrus.wizardz;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SelectUMLPage extends WizardPage {
  private Text ProjectName;
  private Text UmlFile;
  private Button browse1;
  private Composite container;

  public SelectUMLPage() {
    super("Select UML page");
    setTitle("Select UML page");
    setDescription("Give the project name, and browse the source UML file! ");
  }

  @Override
  public void createControl(Composite parent) {
	    container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 3;
	    
	    final Shell shell = container.getShell();
	    
	    
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("Project name: ");
	
	    ProjectName = new Text(container, SWT.BORDER | SWT.SINGLE);
	    ProjectName.setText("");
	    ProjectName.addKeyListener(new KeyListener() {
	
	      @Override
	      public void keyPressed(KeyEvent e) {
	      }
	
	      @Override
	      public void keyReleased(KeyEvent e) {
	        if (!ProjectName.getText().isEmpty() && !UmlFile.getText().isEmpty()) {
	          setPageComplete(true);
	        }
	      }
	
	    });
	    
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    ProjectName.setLayoutData(gd);
	   
	    Label label_blank = new Label(container, SWT.NONE);
	    label_blank.setText("");
	    
	    Label label2 = new Label(container, SWT.NONE);
	    label2.setText("Source UML: ");
	    
	    
	    UmlFile = new Text(container, SWT.BORDER | SWT.SINGLE);
	    UmlFile.setText("");
	    UmlFile.setLayoutData(gd);
	    
	    browse1 = new Button(container,SWT.NONE);
	    browse1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
	    browse1.setText("Browse...");
	    browse1.addKeyListener(new KeyListener() {
	
	        @Override
	        public void keyPressed(KeyEvent e) {
	        }
	
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	SetPageComleteIfReady();
	        }
	
	      });
	    
	    final String[] FILTER_NAMES = {"UML (*.uml)"};
	
	    // These filter extensions are used to filter which files are displayed.
	    final String[] FILTER_EXTS = { "*.uml","*.*"};
	
	    
	    browse1.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	            // User has selected to open a single file
	            FileDialog dlg = new FileDialog(shell, SWT.OPEN);
	            dlg.setFilterNames(FILTER_NAMES);
	            dlg.setFilterExtensions(FILTER_EXTS);
	            String fn = dlg.open();
	            if (fn != null) {
	              UmlFile.setText(fn);
	              SetPageComleteIfReady();
	            }
	          }
	        });
	    
	    // required to avoid an error in the system
	    setControl(container);
	    setPageComplete(false);
  }
  
  private void SetPageComleteIfReady(){
	  if (!ProjectName.getText().isEmpty() && !UmlFile.getText().isEmpty()) {
          setPageComplete(true);
        }
  }
  
  public String getProjectName(){
	 return ProjectName.getText();
  }
  

  public String getUMLPath(){
	 return UmlFile.getText();
  }
}
 