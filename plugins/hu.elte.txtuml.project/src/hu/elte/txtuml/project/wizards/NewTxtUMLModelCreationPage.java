package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
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

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.project.ModelCreator;
import hu.elte.txtuml.utils.platform.PluginLogWrapper;

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
			if (isModelPackage(getPackageFragment())) {
				((StatusInfo) status).setError("The selected package is already a model package");
			}
		}
		return status;
	}

	public static boolean isModelPackage(IPackageFragment pack) {
		try {
			IJavaProject javaProject = pack.getJavaProject();
			String packageName = pack.getElementName();
			for (IPackageFragmentRoot pfRoot : javaProject.getPackageFragmentRoots()) {
				if (!pfRoot.isExternal()) {
					if (isModelPackage(pfRoot, packageName)) {
						return true;
					}
				}
			}
		} catch (JavaModelException e) {
			PluginLogWrapper.logError("Error while checking compilation unit", e);
		}
		return false;
	}

	/**
	 * Checks a package if it belong to an existing model. Searches for a
	 * package-info.java compilation unit in the package or one of the ancestor
	 * packages and checks if it has the {@link Model} annotation.
	 */
	private static boolean isModelPackage(IPackageFragmentRoot pfRoot, String packageName) throws JavaModelException {
		IPackageFragment pack;
		while (!packageName.isEmpty()) {
			pack = pfRoot.getPackageFragment(packageName);
			if (pack.exists() && isModelRootPackage(pack)) {
				return true;
			}
			int lastDot = packageName.lastIndexOf(".");
			if (lastDot == -1) {
				lastDot = 0;
			}
			packageName = packageName.substring(0, lastDot);
		}
		return false;
	}

	private static boolean isModelRootPackage(IPackageFragment pack) throws JavaModelException {
		ICompilationUnit[] compilationUnits = pack.getCompilationUnits();
		for (ICompilationUnit compUnit : compilationUnits) {
			if (compUnit.getElementName().equals("package-info.java")) {
				if (checkPackageInfo(compUnit)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean checkPackageInfo(ICompilationUnit compUnit) throws JavaModelException {
		for (IPackageDeclaration packDecl : compUnit.getPackageDeclarations()) {
			for (IAnnotation annot : packDecl.getAnnotations()) {
				// Because names are not resolved in IJavaElement AST
				// representation, we have to manually check if a given
				// annotation is really the Model annotation.
				if (isImportedNameResolvedTo(compUnit, annot.getElementName(), Model.class.getCanonicalName())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isImportedNameResolvedTo(ICompilationUnit compUnit, String elementName,
			String qualifiedName) {
		if (qualifiedName.equals(elementName)) {
			return true;
		}
		if (!qualifiedName.endsWith(elementName)) {
			return false;
		}
		int lastSection = qualifiedName.lastIndexOf(".");
		String pack = qualifiedName.substring(0, lastSection);
		return (compUnit.getImport(qualifiedName).exists() || compUnit.getImport(pack + ".*").exists());
	}
}
