package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TxtUMLToCppOptionsPage extends WizardPage {

	private static final String browseButtonText = "Browse...";
	
    private Composite composite;
    private GridLayout gridLayout;

    private Text mainCppText;
    private Button mainCppBrowser;
    
    public TxtUMLToCppOptionsPage() {
        super("Generate C++ Code Page");
        setTitle("Generate C++ Code Page");
        super.setDescription("Browse your txtUML project, model and configuration and set specific options to generate C++ code!");
    }

    @Override
    public void createControl(Composite parent) {
    	composite = new Composite(parent, SWT.NONE);
    	gridLayout = new GridLayout();
        composite.setLayout(gridLayout);
        gridLayout.numColumns = 3;
        
        Label mainCppLabel = new Label(composite, SWT.NONE);
        mainCppLabel.setText("Select main file for override: ");
        
        mainCppText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        mainCppText.setText("");
        
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        mainCppText.setLayoutData(gridData);

        mainCppBrowser = new Button(composite, SWT.NONE);
        mainCppBrowser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        mainCppBrowser.setText(browseButtonText);
        mainCppBrowser.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(parent.getShell(), SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath("C:/");
		        fd.setFilterExtensions(new String[] { "*.cpp"});
		        String selected = fd.open();
		        System.out.println(selected);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        
        setControl(composite);
        setPageComplete(true);
    }
}