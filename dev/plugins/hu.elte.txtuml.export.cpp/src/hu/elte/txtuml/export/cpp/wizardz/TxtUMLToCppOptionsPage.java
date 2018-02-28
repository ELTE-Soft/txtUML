package hu.elte.txtuml.export.cpp.wizardz;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class TxtUMLToCppOptionsPage extends WizardPage {

	private static final String browseButtonText = "Browse...";
	
    private Composite composite;
    private GridLayout gridLayout;

    private Text mainCppText;
    private Button mainCppBrowser;
    
    private Text compilerText;
    private Button compilerSelector;
    
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
        
        GridData mainCppGridData = new GridData(GridData.FILL_HORIZONTAL);
        mainCppText.setLayoutData(mainCppGridData);

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
		        mainCppText.setText(selected);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        
        Label emptyRowLabel = new Label(composite, SWT.NONE);
        emptyRowLabel.setText("");
        
        GridData emptryRowGridData = new GridData();
        emptryRowGridData.horizontalAlignment = GridData.FILL;
        emptryRowGridData.horizontalSpan = 3;
        emptyRowLabel.setLayoutData(emptryRowGridData);
        
        Label compilerLabel = new Label(composite, SWT.NONE);
        compilerLabel.setText("Select compilers: ");
        
        compilerText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        compilerText.setText("");
        
        GridData compilerGridData = new GridData(GridData.FILL_HORIZONTAL);
        compilerText.setLayoutData(compilerGridData);
        
        compilerSelector = new Button(composite, SWT.NONE);
        compilerSelector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        compilerSelector.setText(browseButtonText);
        compilerSelector.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] compilers = {"GCC", "Clang", "Visual Studio"};
				ListSelectionDialog dialog = 
						   new ListSelectionDialog(parent.getShell(), compilers, ArrayContentProvider.getInstance(),
						            new LabelProvider(), "Compilers");

						dialog.setTitle("Compiler Selection Dialog");
						dialog.open();
						Object[] selectedCompilers = dialog.getResult();
						
						compilerText.setText(setCompilerText(selectedCompilers));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        
        setControl(composite);
        setPageComplete(true);
    }
    
    private String setCompilerText(Object[] compilers){
    	String result = "";
    	
		for(int i = 0; i < compilers.length; ++i){
			if(i == 0) {
				result += compilers[i].toString();
				continue;
			}
			result += "; " + compilers[i].toString();
		}
		
		return result;
    }
}