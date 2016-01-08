package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.project.ModelCreator;

/**
 * This dialog uses source container, package and type name inputs from
 * {@linkplain NewTypeWizardPage}.
 * 
 * @noextend This class should not be subclassed, subclass
 *           {@linkplain NewTypeWizardPage} instead.
 */
@SuppressWarnings("restriction")
public class NewTxtUMLModelCreationPage extends NewTypeWizardPage {
	protected static final int COLS = 4;
	protected Button txt;
	protected Button xtxt;
	protected boolean xtxtuml;
	private ModelCreator modelCreator = new ModelCreator();

	protected NewTxtUMLModelCreationPage() {
		super(false, TxtUMLModelFileCreatorWizard.TITLE);
		this.setTitle(TxtUMLModelFileCreatorWizard.TITLE);
		this.setDescription(TxtUMLModelFileCreatorWizard.DESCRIPTION);
	}

	@Override
	public void createControl(Composite parent) {
		setControl(createCompositeControl(parent));
	}

	private Composite createCompositeControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = COLS;
		composite.setLayout(layout);
		createContainerControls(composite, COLS);
		createPackageControls(composite, COLS);
		createTypeNameControls(composite, COLS);
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

	/**
	 * Tries to guess the values of container and package from the selected
	 * element. The selected package is assumed to be the model root.
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		if (jelem instanceof IPackageFragmentRoot) {
			setPackageFragmentRoot((IPackageFragmentRoot) jelem, true);
		} else if (jelem instanceof IPackageFragment) {
			IPackageFragment pack = (IPackageFragment) jelem;
			setPackageFragment(pack, true);
			while (jelem instanceof IPackageFragment) {
				jelem = jelem.getParent();
			}
			if (jelem instanceof IPackageFragmentRoot) {
				setPackageFragmentRoot((IPackageFragmentRoot) jelem, true);
			}
		}
	}

	public IFile createNewFile() {
		return modelCreator.createModelFile(getContainer(), getPackageFragmentRoot(), getPackageFragment(),
				getTypeName(), xtxt.getSelection());
	}

	// validation

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		// show error status messages
		doStatusUpdate();
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		// update the status message when a field is changed
		doStatusUpdate();
	}

	protected void doStatusUpdate() {
		IStatus[] status = new IStatus[] { fContainerStatus, fPackageStatus, fTypeNameStatus };
		updateStatus(status);
	}

	@Override
	protected IStatus typeNameChanged() {
		IStatus status = super.typeNameChanged();
		if (status.getMessage() != null
				&& status.getMessage().equals(NewWizardMessages.NewTypeWizardPage_error_EnterTypeName)) {
			((StatusInfo) status).setError("Model name is empty");
		}
		return status;
	}

	@Override
	protected IStatus packageChanged() {
		IStatus status = super.packageChanged();
		if (status.isOK()) {
			if (ElementTypeTeller.isModelPackage(getPackageFragment())) {
				((StatusInfo) status).setError("The selected package is already a model package");
			}
		}
		return status;
	}

}
