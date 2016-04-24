package hu.elte.txtuml.project.wizards.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import hu.elte.txtuml.project.FileCreator;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

/**
 * @noextend This class should not be subclassed, subclass
 *           {@linkplain NewTxtUMLFileElementWizardPage} instead.
 */
@SuppressWarnings("restriction")
public class NewTxtUMLModelWizardPage extends NewTxtUMLFileElementWizardPage {

	public static final String TITLE = "txtUML Model";
	public static final String DESCRIPTION = "Create a new txtUML Model.";
	protected static final int COLS = 4;

	protected Button txt;
	protected Button xtxt;
	private FileCreator fileCreator = new FileCreator();

	public NewTxtUMLModelWizardPage() {
		super(false, TITLE);
		this.setTitle(TITLE);
		this.setDescription(DESCRIPTION);
	}

	@Override
	public void createControl(Composite parent) {
		setControl(createCompositeControl(parent));
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
		createFileTypeChoice(composite, COLS);

		return composite;
	}

	private void createFileTypeChoice(Composite composite, int cols2) {
		Group group1 = new Group(composite, SWT.SHADOW_IN);
		group1.setText("Model syntax");
		group1.setLayout(new RowLayout(SWT.VERTICAL));
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		txt = new Button(group1, SWT.RADIO);
		txt.setText("JtxtUML (Java syntax)");
		txt.setSelection(true);
		xtxt = new Button(group1, SWT.RADIO);
		xtxt.setText("XtxtUML (custom syntax)");
	}

	public IFile createNewFile() {
		return fileCreator.createModelFile(getContainer(), getPackageFragmentRoot(), getPackageFragment(),
				getTypeName(), xtxt.getSelection());
	}

	// validation

	@Override
	protected IStatus typeNameChanged() {
		IStatus status = super.typeNameChanged();
		if (status.getMessage() != null
				&& status.getMessage().equals(NewWizardMessages.NewTypeWizardPage_error_EnterTypeName)) {
			((StatusInfo) status).setError("Model name is empty.");
		}
		return status;
	}

	@Override
	protected IStatus packageChanged() {
		IStatus status = super.packageChanged();
		if (status.isOK()) {
			if (ElementTypeTeller.isModelPackage(getPackageFragment())) {
				((StatusInfo) status).setError("The selected package is already a model package.");
			}
		}
		return status;
	}

}
