package hu.elte.txtuml.export.cpp.wizardz;





import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TxtUMLToCppPage extends WizardPage {
	
	//Layouts
	private Composite composite;
	private GridLayout gridLayout;
	
	private Text txtUMLProject;
	private Text txtUMLModel;
	private Text threadManagerDescription;
	
	Button runtime;
	Button debug;
	
	String tempText;
	
	public static String PROJECT_NAME = "";
	public static String MODEL_NAME = "";
	public static String DESCRIPTION_NAME = "";
	
	
	protected TxtUMLToCppPage(){
		super("Generate C++ Code Page");
		setTitle("Generate C++ Code Page");
		super.setDescription("Give your txtUML project and model class to creat C++ code!");
		
		
	}

	@Override
	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		composite.setLayout(gridLayout);
		gridLayout.numColumns = 2;
		
		
		
		Label projectLabel = new Label(composite, SWT.NONE);
		projectLabel.setText("txtUML Project: ");
		
		txtUMLProject = new Text(composite, SWT.BORDER | SWT.SINGLE);
		txtUMLProject.setText(PROJECT_NAME);
		
		Label modelLabel = new Label(composite, SWT.NONE);
		modelLabel.setText("txtUML Model: ");
		
		txtUMLModel = new Text(composite, SWT.BORDER | SWT.SINGLE);
		txtUMLModel.setText(MODEL_NAME);
		
		Label threadLabel = new Label(composite, SWT.NONE);
		threadLabel.setText("txtUML Threads Description: ");
		
		threadManagerDescription = new Text(composite, SWT.BORDER | SWT.SINGLE);
		threadManagerDescription.setText(DESCRIPTION_NAME);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    txtUMLModel.setLayoutData(gd);
	    txtUMLProject.setLayoutData(gd);
	    threadManagerDescription.setLayoutData(gd);
		
		runtime = new Button(composite, SWT.CHECK);
		runtime.setText("Set runtime option");
		
	    debug = new Button(composite, SWT.CHECK);
	    debug.setText("Set debug option");
	    
		 setControl(composite);
		 setPageComplete(true);
		
		
	}
	
	public String getProject(){
		return txtUMLProject.getText();
	}
	
	public String getModel(){
		return txtUMLModel.getText();
	}
	
	public String getThreadDescription(){
		return threadManagerDescription.getText();
	}
	
	public boolean selectRuntimeOption(){
		return runtime.getSelection();
	}
	
	public boolean selectDebugOption(){
		return debug.getSelection();
	}
}
