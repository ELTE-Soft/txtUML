package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TxtUMLProjectPage extends WizardPage {
	private Text projectName;
	private Text modelName;

	private static final String defaultModelName = "Sample";
	private static final String defaultProjectName = "project.sample";

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public TxtUMLProjectPage() {
		super("txtUML Project creation page");
		setTitle("Creating txtUML Project");
		setDescription("Give the name of the new Project and the name of your txtUML model!");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project Name:");

		projectName = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectName.setLayoutData(gd);
		projectName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Model Name:");

		modelName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		modelName.setLayoutData(gd);
		modelName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		projectName.setText(TxtUMLProjectPage.defaultProjectName);
		modelName.setText(TxtUMLProjectPage.defaultModelName);
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		String projectname = getProjectName();
		if (projectname.length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(projectname);
		if (project.exists()) {
			updateStatus("This project already exists");
			return;
		}
		if (getModelName().length() == 0) {
			updateStatus("Model name must be specified");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectName.getText();
	}

	public String getModelName() {
		return modelName.getText();
	}
}