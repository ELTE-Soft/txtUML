package hu.elte.txtuml.utils.eclipse.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.eclipse.NotFoundException;
import hu.elte.txtuml.utils.eclipse.PackageUtils;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;

public class VisualizeTxtUMLPage extends WizardPage {

	private final Class<?>[] diagramTypes;
	private Composite container;
	private List<IType> txtUMLLayout = new LinkedList<>();
	private ScrolledComposite sc;
	private CheckboxTreeViewer tree;
	private final boolean directSuperClasses;
	private final boolean progressBar;

	/**
	 * The Constructor
	 */
	public VisualizeTxtUMLPage(boolean progressBar, boolean directSuperClasses, Class<?>... diagramTypes) {
		super("Visualize txtUML page");
		this.diagramTypes = diagramTypes;
		this.directSuperClasses = directSuperClasses;
		this.progressBar = progressBar;
		setTitle("Visualize txtUML page");
		setDescription("Select the diagrams to be visualized.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		if (progressBar) {
			IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
			try {
				progressService.runInUI(progressService, new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						createPage(parent, monitor);
					}

				}, ResourcesPlugin.getWorkspace().getRoot());
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.sys.error(e.getMessage());
			}
		} else {
			createPage(parent, null);
		}
	}

	/**
	 * Returns the txtUML model layout classes
	 * 
	 * @return
	 */
	public List<IType> getTxtUmlLayouts() {
		List<IType> result = new ArrayList<IType>();
		for (IType layout : txtUMLLayout) {
			if (!"".equals(layout.getFullyQualifiedName())) {
				result.add(layout);
			}
		}
		return result;
	}

	/**
	 * Selects the given elements in the diagram selection tree
	 * 
	 * @param elements
	 *            the elements to select in tree
	 * @param isTreeVisible
	 *            is the selection tree visible at the moment
	 */
	public void selectElementsInDiagramTree(Object[] elements, boolean isTreeVisible) {
		if (!isTreeVisible)
			tree.expandAll();

		txtUMLLayout.clear();
		tree.setCheckedElements(elements);
		List<IType> checkedTypes = Arrays.asList(elements).stream().filter(e -> e instanceof IType).map(e -> (IType) e)
				.collect(Collectors.toList());

		checkedTypes.forEach(type -> txtUMLLayout.add(type));
		Stream.of(tree.getTree().getItems()).forEach(pr -> updateParentCheck(pr));

		if (!isTreeVisible)
			tree.collapseAll();
	}

	/**
	 * Expands the given layouts in the layout selection tree
	 * 
	 * @param expandableTypes
	 *            the types to expand in tree
	 */
	public void setExpandedLayouts(List<IType> typesToExpand) {
		tree.setExpandedElements(typesToExpand.stream().map(type -> type.getJavaProject()).toArray());
	}

	private void selectionHandler(Event event) {
		if (event.detail == SWT.CHECK) {
			TreeItem item = (TreeItem) event.item;
			if (item.getData() instanceof IType) {
				IType type = (IType) item.getData();
				if (!txtUMLLayout.contains(type)) {
					txtUMLLayout.add(type);
				} else {
					txtUMLLayout.remove(type);
				}
				updateParentCheck(item.getParentItem());
			} else {
				checkItems(item, item.getChecked());
			}
		}
	}

	private List<IType> getDiagramDescriptions(IPackageFragment packageFragment) {
		if (directSuperClasses) {
			return WizardUtils.getTypesByDirectSuperclass(packageFragment, diagramTypes);
		} else {
			return WizardUtils.getTypesBySuperclass(packageFragment, diagramTypes);
		}
	}

	private void addInitialLayoutFields() {
		List<String> layouts = new ArrayList<>(
				PreferencesManager.getStrings(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT));
		List<String> layoutProjects = new ArrayList<>(
				PreferencesManager.getStrings(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT_PROJECTS));

		if (layouts.size() == layoutProjects.size()) {
			for (int layoutNo = 0; layoutNo < layouts.size(); ++layoutNo) {
				String layout = layouts.get(layoutNo);
				String layoutProject = layoutProjects.get(layoutNo);
				if (!layout.isEmpty()) {
					addLayoutField(layout, layoutProject);
				}
			}
		}
	}

	private void addLayoutField(String qualifiedName, String layoutProject) {
		try {
			IJavaProject layoutJavaProject = ProjectUtils.findJavaProject(layoutProject);
			List<IType> types = PackageUtils.findAllPackageFragmentsAsStream(layoutJavaProject)
					.flatMap(pf -> getDiagramDescriptions(pf).stream()).collect(Collectors.toList());
			types.stream().filter(type -> type.getFullyQualifiedName().equals(qualifiedName))
					.forEach(type -> txtUMLLayout.add(type));
		} catch (NotFoundException | JavaModelException ex) {
			Logger.sys.error(ex.getMessage());
		}
	}

	private void updateParentCheck(TreeItem parentItem) {
		boolean isAnyChecked = Stream.of(parentItem.getItems()).anyMatch(child -> child.getChecked());
		boolean isAllChecked = Stream.of(parentItem.getItems()).allMatch(child -> child.getChecked());

		if (isAllChecked) {
			parentItem.setGrayed(false);
			parentItem.setChecked(true);
		} else if (isAnyChecked) {
			parentItem.setChecked(true);
			parentItem.setGrayed(true);
		} else {
			parentItem.setGrayed(false);
			parentItem.setChecked(false);
		}
	}

	private void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; ++i) {
			TreeItem child = items[i];
			child.setChecked(checked);
			IType childData = (IType) child.getData();
			if (checked) {
				if (!txtUMLLayout.contains(childData)) {
					txtUMLLayout.add(childData);
				}
			} else {
				txtUMLLayout.remove(childData);
			}
		}
	}

	private void createPage(Composite parent, IProgressMonitor monitor) {
		if (monitor != null)
			monitor.beginTask("Creating dialog", 100);

		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		container = new Composite(sc, SWT.NONE);

		GridLayout layout = new GridLayout(4, false);
		container.setLayout(layout);

		final Label label = new Label(container, SWT.TOP);
		label.setText("txtUML Diagrams: ");

		addInitialLayoutFields();

		// diagram descriptions tree
		ScrolledComposite treeComposite = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tree = getDiagramTreeViewer(treeComposite, monitor);
		tree.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				Iterator<?> selectedElements = ((IStructuredSelection) selection).iterator();
				if (selectedElements.hasNext()) {
					Object selectedElement = selectedElements.next();
					if (selectedElement instanceof IJavaProject) {
						List<Object> expandedElements = new ArrayList<>(Arrays.asList(tree.getExpandedElements()));
						if (expandedElements.contains(selectedElement)) {
							expandedElements.remove(selectedElement);
						} else {
							expandedElements.add(selectedElement);
						}
						tree.setExpandedElements(expandedElements.toArray());
					} else if (selectedElement instanceof IType) {
						List<Object> checkedElements = new ArrayList<>(Arrays.asList(tree.getCheckedElements()));
						boolean isChecked = checkedElements.contains(selectedElement);
						tree.setChecked(selectedElement, !isChecked);
						IType selectedType = (IType) selectedElement;
						if (!isChecked && !txtUMLLayout.contains(selectedType)) {
							txtUMLLayout.add(selectedType);
						} else {
							txtUMLLayout.remove(selectedType);
						}
						selectElementsInDiagramTree(txtUMLLayout.toArray(), true);
					}
				}
			}
		});

		selectElementsInDiagramTree(txtUMLLayout.toArray(), false);
		setExpandedLayouts(txtUMLLayout);

		GridData treeGd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		treeGd.heightHint = 200;
		treeGd.widthHint = 150;
		GridData labelGd = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		labelGd.verticalIndent = 5;
		label.setLayoutData(labelGd);
		treeComposite.setLayoutData(treeGd);

		treeComposite.setContent(tree.getControl());
		treeComposite.setExpandHorizontal(true);
		treeComposite.setExpandVertical(true);
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		container.setSize(container.computeSize(450, 300, true));
		sc.setMinSize(container.getSize());
		sc.setSize(container.getSize());

		setControl(parent);
		setPageComplete(true);
		if (monitor != null)
			monitor.done();
	}

	private CheckboxTreeViewer getDiagramTreeViewer(ScrolledComposite treeComposite, IProgressMonitor monitor) {
		CheckboxTreeViewer tree = new CheckboxTreeViewer(treeComposite, SWT.NONE);
		IContentProvider cp = new WorkbenchContentProvider() {
			private int projectNum;

			@Override
			public Object[] getChildren(Object element) {
				if (element instanceof IWorkspaceRoot) {
					List<IJavaProject> javaProjects = new ArrayList<>();
					IProject[] allProjects = ((IWorkspaceRoot) element).getProjects();
					projectNum = allProjects.length;
					for (IProject pr : allProjects) {
						try {
							IJavaProject javaProject = ProjectUtils.findJavaProject(pr.getName());
							if (directSuperClasses
									&& WizardUtils.containsClassesWithDirectSuperTypes(javaProject, diagramTypes)) {
								javaProjects.add(ProjectUtils.findJavaProject(pr.getName()));
							} else if (WizardUtils.containsClassesWithSuperTypes(javaProject, diagramTypes)) {
								javaProjects.add(ProjectUtils.findJavaProject(pr.getName()));
							}
							if (monitor != null)
								monitor.worked(80 / projectNum);
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
					List<IType> descriptionTypes = packageFragments.stream()
							.flatMap(pf -> getDiagramDescriptions(pf).stream()).collect(Collectors.toList());
					return descriptionTypes.toArray();
				}
				if (monitor != null)
					monitor.worked(20 / projectNum);

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
		};

		tree.getTree().addListener(SWT.Selection, event -> selectionHandler(event));
		tree.setContentProvider(cp);
		tree.setLabelProvider(WizardUtils.getPostQualifiedLabelProvider());
		tree.setInput(ResourcesPlugin.getWorkspace().getRoot());
		return tree;
	}
}
