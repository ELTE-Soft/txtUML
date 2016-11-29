package hu.elte.txtuml.utils.eclipse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
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

public final class SaveUtils {
	private static HashSet<String> getPackageElements(String projectName, String modelName, List<String> descriptions) {
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
	
	private static ArrayList<IEditorPart> getEffectedDirtyEditors(String projectName, String modelName, List<String> descriptions) {
		HashSet<String> packageElements = getPackageElements(projectName, modelName, descriptions);
		IEditorPart[] dirtyEditors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors();
		ArrayList<IEditorPart> editors = new ArrayList<IEditorPart>(dirtyEditors.length);
		
		for (IEditorPart part : dirtyEditors) {
			IEditorInput input = part.getEditorInput();
			
            if (input instanceof IFileEditorInput) {
            	String packageName = JavaCore.create(((IFileEditorInput) input).getFile().getParent()).getElementName();
            	String fileName = input.getName();
				String fullName = packageName + "." + fileName;
				
				if (packageElements.contains(fullName)) {
					editors.add(part);
				}
            }
		}
		
		return editors;
	}
	
	private static ILabelProvider getLabelProvider() {
		return new ILabelProvider() {
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			} 
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public String getText(Object element) {
				return ((IEditorPart)element).getEditorInput().getName();
			}
			
			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	public static boolean Save(Shell shell, String projectName, String modelName, List<String> descriptions) {
		NullProgressMonitor monitor = new NullProgressMonitor();
		ArrayList<IEditorPart> editors = getEffectedDirtyEditors(projectName, modelName, descriptions);
		
		if (!editors.isEmpty()) {
			ListSelectionDialog lsd = new ListSelectionDialog(shell, editors, new ArrayContentProvider(), getLabelProvider() , "Select resources to save:");
		    lsd.setInitialSelections(editors.toArray());
		    lsd.setTitle("Save and Launch");
		    lsd.open();
		    
		    Object[] results = lsd.getResult();
		    if (results == null)
		    	return false;
		    
		    for (Object o : results) {
		    	((IEditorPart)o).doSave(monitor);
		    }
		}
	    return true;
	}


	public static boolean Save(Shell shell, String projectName, String modelName, String description) {
		List<String> descriptions = new LinkedList<String>();
		descriptions.add(description);
		
		return Save(shell, projectName, modelName, descriptions);
	}
}
