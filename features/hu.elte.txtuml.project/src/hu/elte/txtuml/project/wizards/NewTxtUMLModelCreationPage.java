package hu.elte.txtuml.project.wizards;


import hu.elte.txtuml.eclipseutils.Dialogs;

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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

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
		createSeparator(composite, COLS);
		createTypeNameControls(composite, COLS);
		createSeparator(composite, COLS);
		createFileTypeChoice(composite, COLS);
		return composite;
	}

	private void createFileTypeChoice(Composite composite, int cols2) {
		Group group1 = new Group(composite, SWT.SHADOW_IN);
	    group1.setText("Model syntax");
	    group1.setLayout(new RowLayout(SWT.VERTICAL));
	    group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3,1));
	    txt = new Button(group1, SWT.RADIO);
	    txt.setText("JTxtUML (Java syntax)");
	    txt.setSelection(true);
	    xtxt = new Button(group1, SWT.RADIO);
	    xtxt.setText("XtxtUML (custom syntax)");
	}

	protected void doStatusUpdate() {
		IStatus[] status = new IStatus[] { fContainerStatus, fPackageStatus, fTypeNameStatus };
		updateStatus(status);
	}
	
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
	}

	public IFile createNewFile() {
		String extension;
		String content;
		String pack = getPackageFragment().getElementName();
		if(xtxt.getSelection()){
			extension = ".xtxtuml";
			content = "".equals(pack) ? "" : "package "+pack+";"+System.lineSeparator();
			content += System.lineSeparator()
					+"model "+getTypeName()+" {"+System.lineSeparator()
					+"\t"+System.lineSeparator()
					+"}";
		}else{
			extension = ".java";
			content = "".equals(pack) ? "" : "package "+pack+";"+System.lineSeparator();
			content += System.lineSeparator()
					+"import hu.elte.txtuml.api.model.*;"+System.lineSeparator()
					+System.lineSeparator()
					+"class "+getTypeName()+" extends Model {"+System.lineSeparator()
					+"\t"+System.lineSeparator()
					+"}";
		}
		
		if(createFile(content, extension)){
			return (IFile) getResource();
		}else{
			return null;
		}
	}
	
	private IResource getResource() {
		return this.resource;
	}

	private boolean createFile(String content, String extension){
		IRunnableWithProgress op = new WorkspaceModifyOperation() {
			
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException,
					InvocationTargetException, InterruptedException {
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
					IFile txtUMLFile = ((IFolder) res).getFile(getTypeName() + extension); //$NON-NLS-1$
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
			Dialogs.errorMsgb("File Creation error", realException.getMessage(), realException);
		}
		return true;
	}

	private void setResource(IResource resource) {
		this.resource = resource;
	}
	
	@Override
	protected IStatus typeNameChanged() {
		IStatus status = super.typeNameChanged();
		if(status.getMessage() != null && status.getMessage().equals(NewWizardMessages.NewTypeWizardPage_error_EnterTypeName)){
			((StatusInfo) status).setError("Model name is empty");
		}
		return status;
	}
}
