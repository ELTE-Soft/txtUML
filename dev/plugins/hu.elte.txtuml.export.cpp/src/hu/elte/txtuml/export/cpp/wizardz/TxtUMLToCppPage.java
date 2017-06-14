package hu.elte.txtuml.export.cpp.wizardz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.utils.eclipse.NotFoundException;
import hu.elte.txtuml.utils.eclipse.PackageUtils;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;

@SuppressWarnings("restriction")
public class TxtUMLToCppPage extends WizardPage {

	private static final String browseButtonText = "Browse...";

	// Layouts
	private Composite composite;
	private GridLayout gridLayout;

	private Text threadManagerDescriptionText;
	private static IType threadManagerDescription;

	private Button descriptionBrowser;

	private Button addRuntime;
	private Button overWriteMainFle;

	private static String DESCRIPTION_NAME = "";

	protected TxtUMLToCppPage() {
		super("Generate C++ Code Page");
		setTitle("Generate C++ Code Page");
		super.setDescription("Browse your txtUML project, model and configuration to generate C++ code!");
	}

	@Override
	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		composite.setLayout(gridLayout);
		gridLayout.numColumns = 3;

		Label threadLabel = new Label(composite, SWT.NONE);
		threadLabel.setText("txtUML Deployment configuration: ");

		threadManagerDescriptionText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		threadManagerDescriptionText.setText(DESCRIPTION_NAME);

		descriptionBrowser = new Button(composite, SWT.NONE);
		descriptionBrowser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		descriptionBrowser.setText(browseButtonText);
		descriptionBrowser.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = getConfigurationSelectionDialog();
				dialog.setAllowMultiple(false);
				dialog.setValidator(new TypedElementSelectionValidator(new Class<?>[] { IType.class }, false));
				dialog.setTitle("Configuration selection");
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.setStatusLineAboveButtons(false);
				dialog.open();
				Object[] result = dialog.getResult();
				if (result != null && result.length > 0 && result[0] instanceof IType) {
					IType item = (IType) result[0];
					threadManagerDescriptionText.setText(item.getFullyQualifiedName());
					threadManagerDescription = item;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		threadManagerDescriptionText.setLayoutData(gd);

		addRuntime = new Button(composite, SWT.CHECK);
		addRuntime.setSelection(true);
		addRuntime.setText("Copy runtime next to generated files");

		overWriteMainFle = new Button(composite, SWT.CHECK);
		overWriteMainFle.setSelection(false);
		overWriteMainFle.setText("Overwrite main file if it exists");

		setControl(composite);
		setPageComplete(true);
	}

	public IType getThreadDescription() {
		return threadManagerDescription;
	}

	public static void setThreadManagerDescription(IType threadManagerDescription) {
		TxtUMLToCppPage.threadManagerDescription = threadManagerDescription;
		DESCRIPTION_NAME = threadManagerDescription.getFullyQualifiedName();
	}

	public boolean getAddRuntimeOptionSelection() {
		return addRuntime.getSelection();
	}

	public boolean getOverWriteMainFileSelection() {
		return overWriteMainFle.getSelection();
	}

	private ElementTreeSelectionDialog getConfigurationSelectionDialog() {
		return new ElementTreeSelectionDialog(composite.getShell(), WizardUtils.getPostQualifiedLabelProvider(),
				new WorkbenchContentProvider() {
					@Override
					public Object[] getChildren(Object element) {
						if (element instanceof IWorkspaceRoot) {
							List<IJavaProject> javaProjects = new ArrayList<>();
							IProject[] allProjects = ((IWorkspaceRoot) element).getProjects();
							for (IProject pr : allProjects) {
								try {
									IJavaProject javaProject = ProjectUtils.findJavaProject(pr.getName());
									if (WizardUtils.containsClassesWithSuperTypes(javaProject, Configuration.class)) {
										javaProjects.add(javaProject);
									}
								} catch (NotFoundException e) {
								}
							}
							return javaProjects.toArray();
						}
						if (element instanceof IJavaProject) {
							List<IPackageFragment> packageFragments = null;
							try {
								packageFragments = PackageUtils.findAllPackageFragmentsAsStream((IJavaProject) element)
										.collect(Collectors.toList());
							} catch (JavaModelException ex) {
							}
							List<IType> configTypes = packageFragments.stream()
									.flatMap(pf -> WizardUtils.getTypesBySuperclass(pf, Configuration.class).stream())
									.collect(Collectors.toList());
							return configTypes.toArray();
						}
						return new Object[0];
					}

					@Override
					public Object[] getElements(Object inputElement) {
						return getChildren(inputElement);
					}

					@Override
					public Object getParent(Object element) {
						if (element instanceof IResource) {
							return ((IResource) element).getParent();
						}
						return null;
					}

					@Override
					public boolean hasChildren(Object element) {
						try {
							return getChildren(element).length > 0;
						} catch (NullPointerException ex) {
							return false;
						}
					}
				});
	}

}
