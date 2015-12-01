package hu.elte.txtuml.project.wizards;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.diagnostics.PluginLogWrapper;
import hu.elte.txtuml.eclipseutils.Dialogs;

@SuppressWarnings("restriction")
public class NewTxtUMLModelCreationPage extends NewTypeWizardPage {
	protected static final int COLS = 4;
	protected Button txt;
	protected Button xtxt;
	protected boolean xtxtuml;
	private IResource resource;

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

	protected void doStatusUpdate() {
		IStatus[] status = new IStatus[] { fContainerStatus };
		updateStatus(status);
	}

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

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
	}

	public IFile createNewFile() {
		try {
			String filename;
			StringBuilder contentBuilder = new StringBuilder();
			if (xtxt.getSelection()) {
				filename = "model-info.xtxtuml";
				contentBuilder.append("model \"");
				contentBuilder.append(getTypeName());
				contentBuilder.append("\";");
			} else {
				filename = "package-info.java";
				contentBuilder.append("@Model(\"");
				contentBuilder.append(getTypeName());
				contentBuilder.append("\")\npackage ");
				contentBuilder.append(getPackageText());
				contentBuilder.append(";\n\n");
				contentBuilder.append("import ");
				contentBuilder.append(Model.class.getCanonicalName());
				contentBuilder.append(";");
			}

			if (createFile(contentBuilder.toString(), filename)) {
				return (IFile) getResource();
			}
		} catch (Throwable e) {
			PluginLogWrapper.logError("Error while creating package/model-info file", e);
		}
		return null;
	}

	private IResource getResource() {
		return this.resource;
	}

	private boolean createFile(String content, String filename) {
		IRunnableWithProgress op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException, InterruptedException {
				if (monitor == null) {
					monitor = new NullProgressMonitor();
				}

				try {
					if (!getPackageFragment().exists()) {
						try {
							getPackageFragmentRoot().createPackageFragment(getPackageFragment().getElementName(), true,
									monitor);
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
					}
					IResource res = getPackageFragment().getResource();
					IFile txtUMLFile = ((IFolder) res).getFile(filename);
					txtUMLFile.create(new ByteArrayInputStream(content.getBytes()), true, monitor);
					setResource(txtUMLFile);
				} catch (OperationCanceledException e) {
					throw new InterruptedException();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}

		};
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			// cancelled by user
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			Dialogs.errorMsgb("File Creation error", "Error occured during file creation. ", realException);
		}
		return true;
	}

	private void setResource(IResource resource) {
		this.resource = resource;
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
}
