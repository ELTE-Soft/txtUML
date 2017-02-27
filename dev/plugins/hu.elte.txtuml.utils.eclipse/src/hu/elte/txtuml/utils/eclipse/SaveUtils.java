package hu.elte.txtuml.utils.eclipse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public final class SaveUtils {
	public static HashSet<String> getPackageElements(String projectName, String modelName,
			Iterable<String> descriptions) {
		HashSet<String> packageElements = new HashSet<String>();

		for (String description : descriptions) {
			packageElements.add(description + ".java");
		}

		try {
			IPackageFragment[] packages = PackageUtils.findPackageFragments(projectName, modelName);

			for (IPackageFragment pack : packages) {
				String packageName = pack.getElementName();

				for (IJavaElement elem : pack.getChildren()) {
					String fileName = elem.getElementName();
					String fullName = packageName + "." + fileName;

					packageElements.add(fullName);
				}
			}
		} catch (JavaModelException | NotFoundException ex) {
		}

		return packageElements;
	}

	public static ArrayList<IEditorPart> getAffectedDirtyEditors(String projectName, String modelName,
			Iterable<String> descriptions) {
		HashSet<String> packageElements = getPackageElements(projectName, modelName, descriptions);
		IEditorPart[] dirtyEditors = getDitryEditors();
		ArrayList<IEditorPart> affectedEditors = new ArrayList<IEditorPart>(dirtyEditors.length);

		for (IEditorPart part : dirtyEditors) {
			IEditorInput input = part.getEditorInput();

			if (input instanceof IFileEditorInput) {
				String packageName = JavaCore.create(((IFileEditorInput) input).getFile().getParent()).getElementName();
				String fileName = input.getName();
				String fullName = packageName + "." + fileName;

				if (packageElements.contains(fullName)) {
					affectedEditors.add(part);
				}
			}
		}

		return affectedEditors;
	}

	public static IEditorPart[] getDitryEditors() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors();
	}

	public static boolean saveAffectedFiles(Shell shell, String projectName, String modelName, String description) {
		ArrayList<String> descriptions = new ArrayList<String>();
		descriptions.add(description);

		return saveAffectedFiles(shell, projectName, modelName, descriptions);
	}

	public static boolean saveAffectedFiles(Shell shell, String projectName, String modelName,
			Iterable<String> descriptions) {
		ArrayList<IEditorPart> affectedEditors = getAffectedDirtyEditors(projectName, modelName, descriptions);

		return save(shell, affectedEditors);
	}

	public static boolean saveAllFiles(Shell shell) {
		IEditorPart[] dirtyEditors = getDitryEditors();

		return save(shell, Arrays.asList(dirtyEditors));
	}

	private static boolean save(Shell shell, Collection<IEditorPart> editors) {
		if (!editors.isEmpty()) {
			if (!saveAutomatically()) {
				Object[] result = openSaveDialog(shell, editors);
				if (result == null)
					return false;
				saveEditors(Arrays.asList(result));
			} else {
				saveEditors(editors);
			}
		}
		return true;
	}

	public static boolean saveAutomatically() {
		IPreferenceStore s = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.ide");
		return s.contains("SAVE_ALL_BEFORE_BUILD") && s.getBoolean("SAVE_ALL_BEFORE_BUILD");
	}

	private static Object[] openSaveDialog(Shell shell, Collection<IEditorPart> editors) {
		ListSelectionDialog lsd = new ListSelectionDialog(shell, editors, new ArrayContentProvider(),
				getLabelProvider(), "Select resources to save:");
		lsd.setInitialSelections(editors.toArray());
		lsd.setTitle("Save and Launch");
		lsd.open();

		return lsd.getResult();
	}

	private static ILabelProvider getLabelProvider() {
		return new ILabelProvider() {
			@Override
			public void removeListener(ILabelProviderListener listener) {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public String getText(Object element) {
				return ((IEditorPart) element).getEditorInput().getName();
			}

			@Override
			public Image getImage(Object element) {
				return null;
			}
		};
	}

	public static <T> void saveEditors(Iterable<T> editors) {
		NullProgressMonitor monitor = new NullProgressMonitor();
		for (T e : editors) {
			((IEditorPart) e).doSave(monitor);
		}
	}
}