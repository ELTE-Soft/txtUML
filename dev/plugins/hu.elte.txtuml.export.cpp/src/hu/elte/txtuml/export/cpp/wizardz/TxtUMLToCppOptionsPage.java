package hu.elte.txtuml.export.cpp.wizardz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import hu.elte.txtuml.utils.eclipse.Dialogs;

public class TxtUMLToCppOptionsPage extends WizardPage {

	private static final String browseButtonText = "Browse...";
	
    private Composite composite;
    private GridLayout gridLayout;

    private Text mainCppText;
    private Button mainCppBrowser;
    
    private Text buildEnvironmentText;
    private Button buildEnvironmentSelector;
    private List<String> buildEnvironments;
    
    private static List<String> initEnvironments = Arrays.asList(
			"Visual Studio 15 2017",
			"Visual Studio 14 2015",
			"Visual Studio 12 2013",
			"MinGW Makefiles",
			"Unix Makefiles",
			"Ninja",
			"CodeBlocks - MinGW Makefile",
			"CodeBlocks - Ninja",
			"CodeBlocks - Unix Makefiles",
			"Eclipse CDT4 - MinGW Makefiles",
			"Eclipse CDT4 - Ninja",
			"Eclipse CDT4 - Unix Makefiles"
	);
    
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
        mainCppLabel.setText("New main file: ");
        
        mainCppText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        mainCppText.setText("");
        
        GridData mainCppGridData = new GridData(GridData.FILL_HORIZONTAL);
        mainCppText.setLayoutData(mainCppGridData);
        mainCppText.setEnabled(false);

        mainCppBrowser = new Button(composite, SWT.NONE);
        mainCppBrowser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        mainCppBrowser.setText(browseButtonText);
        mainCppBrowser.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(parent.getShell(), SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterExtensions(new String[] { "*.cpp"});
		        String selected = fd.open();
	        	mainCppText.setText(selected);

		        if(!selected.contains("main.cpp") && 
		        	!Dialogs.WarningConfirm("File selector warning", "Your file's name is not 'main.cpp'!\nPlease make sure that it contains main function!\nWould you like to override main with this file?")) {
		        		mainCppText.setText("");
		        }
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
        
        Label buildEnvironmentLabel = new Label(composite, SWT.NONE);
        buildEnvironmentLabel.setText("Build environments: ");
        
        buildEnvironmentText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        buildEnvironmentText.setText("");
        buildEnvironmentText.setEnabled(false);
        
        GridData buildEnvironmentGridData = new GridData(GridData.FILL_HORIZONTAL);
        buildEnvironmentText.setLayoutData(buildEnvironmentGridData);
        
        buildEnvironmentSelector = new Button(composite, SWT.NONE);
        buildEnvironmentSelector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        buildEnvironmentSelector.setText(browseButtonText);
        buildEnvironmentSelector.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ListSelectionDialog dialog = 
						   new ListSelectionDialog(parent.getShell(), initEnvironments, ArrayContentProvider.getInstance(),
						            new LabelProvider(), "Build environments");

						dialog.setTitle("Build Environment Selection Dialog");
						dialog.open();
						Object[] selectedEnvironments = dialog.getResult();
						
						buildEnvironmentText.setText(setBuildEnvironmentText(selectedEnvironments));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        
        setControl(composite);
        setPageComplete(true);
    }
    
    private String setBuildEnvironmentText(Object[] environments){
    	StringBuilder result = new StringBuilder();
    	String actualEnvironment = "";
    	buildEnvironments = new ArrayList<String>();
    	
		for(int i = 0; i < environments.length; ++i){
			actualEnvironment = environments[i].toString();
			buildEnvironments.add(actualEnvironment);
			if(i == 0) {
				result.append(actualEnvironment);
				continue;
			}
			result.append("; ");
			result.append(actualEnvironment);
		}
		
		return result.toString();
    }
    
    public List<String> getSelectedBuildEnvironments() {
    	return buildEnvironments;
    }
    
    public String getMainForOverride() {
    	return mainCppText.getText();
    }
}
