package hu.elte.txtuml.project.wizards.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

/**
 * This dialog uses source container, package and type name inputs from
 * {@linkplain NewTypeWizardPage}.
 */
@SuppressWarnings("restriction")
public abstract class NewTxtUMLFileElementWizardPage extends NewTypeWizardPage {

	protected NewTxtUMLFileElementWizardPage(boolean isClass, String pageName) {
		super(isClass, pageName);
	}

	@Override
	public void createControl(Composite parent) {
		setControl(createCompositeControl(parent));
	}

	protected abstract Composite createCompositeControl(Composite parent);

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
	protected IStatus packageChanged() {
		StatusInfo status = (StatusInfo) super.packageChanged();
		if (status.getMessage() != null
				&& status.getMessage().equals(NewWizardMessages.NewTypeWizardPage_warning_DefaultPackageDiscouraged)) {
			status.setError("The default package cannot be used in txtUML.");
		}

		return status;
	}

	@Override
	protected IStatus typeNameChanged() {
		StatusInfo status = (StatusInfo) super.typeNameChanged();
		String message = status.getMessage();

		if (message != null) {
			if (message
					.startsWith(Messages.format(NewWizardMessages.NewTypeWizardPage_warning_TypeNameDiscouraged, ""))) {
				status.setOK(); // it's ok if the name starts with a lowercase
								// letter
			} else if (message
					.startsWith(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidTypeName, ""))) {
				status.setError(message.replace("Type name", "Name").replace("Java type name", "name")
						.replace("type name", "name"));
				// errors about *type* names would be confusing here
			}
		}

		return status;
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete() && getPackageFragmentRoot() != null && getPackageFragment() != null
				&& !getPackageFragment().isDefaultPackage() && getTypeName() != null;
		// all fields are required
	}

}
