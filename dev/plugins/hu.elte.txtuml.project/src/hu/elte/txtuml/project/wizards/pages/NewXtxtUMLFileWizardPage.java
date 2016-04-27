package hu.elte.txtuml.project.wizards.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import hu.elte.txtuml.project.FileCreator;

/**
 * @noextend This class should not be subclassed, subclass
 *           {@linkplain NewTxtUMLFileElementWizardPage} instead.
 */
@SuppressWarnings("restriction")
public class NewXtxtUMLFileWizardPage extends NewTxtUMLFileElementWizardPage {

	public static final String TITLE = "XtxtUML File";
	public static final String DESCRIPTION = "Create a new XtxtUML file.";
	protected static final int COLS = 4;

	private FileCreator fileCreator = new FileCreator();
	private Button xExtensionOption;

	public NewXtxtUMLFileWizardPage() {
		super(false, TITLE);
		this.setTitle(TITLE);
		this.setDescription(DESCRIPTION);
	}

	@Override
	protected Composite createCompositeControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = COLS;
		composite.setLayout(layout);

		createContainerControls(composite, COLS);
		createPackageControls(composite, COLS);
		createTypeNameControls(composite, COLS);
		new Separator(SWT.HORIZONTAL).doFillIntoGrid(composite, COLS, convertHeightInCharsToPixels(0));
		createExtensionControls(composite, COLS);

		return composite;
	}

	private void createExtensionControls(Composite composite, int cols) {
		Group group = new Group(composite, SWT.SHADOW_IN);
		group.setText("File extension");
		group.setLayout(new RowLayout(SWT.VERTICAL));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));

		// check for existing files if the user chooses another extension
		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fTypeNameStatus = typeNameChanged();
				doStatusUpdate();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				fTypeNameStatus = typeNameChanged();
				doStatusUpdate();
			}
		};

		xExtensionOption = new Button(group, SWT.RADIO);
		xExtensionOption.setText(".xtxtuml");
		xExtensionOption.setSelection(true);
		xExtensionOption.addSelectionListener(listener);

		Button tExtensionOption = new Button(group, SWT.RADIO);
		tExtensionOption.setText(".txtuml");
		tExtensionOption.addSelectionListener(listener);
	}

	private String getSelectedExtension() {
		return xExtensionOption == null || xExtensionOption.getSelection() ? ".xtxtuml" : ".txtuml";
	}

	public IFile createNewFile() {
		return fileCreator.createXtxtUmlFile(getContainer(), getPackageFragmentRoot(), getPackageFragment(),
				getTypeName() + getSelectedExtension());
	}

	@Override
	protected IStatus typeNameChanged() {
		StatusInfo status = (StatusInfo) super.typeNameChanged();
		String message = status.getMessage();

		if (message != null && message.equals(NewWizardMessages.NewTypeWizardPage_error_EnterTypeName)) {
			status.setError("Filename is empty.");
		}

		if (getPackageFragment() != null && getTypeName() != null) {
			IFolder folder = (IFolder) getPackageFragment().getResource();
			IFile file = folder.getFile(getTypeName() + getSelectedExtension());
			if (file.exists()) {
				status.setError("File already exists.");
			}
		}

		return status;
	}

}
