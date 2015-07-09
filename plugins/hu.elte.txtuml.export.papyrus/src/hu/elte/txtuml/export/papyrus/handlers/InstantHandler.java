package hu.elte.txtuml.export.papyrus.handlers;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handles the the call to visualization from context menu.
 *
 * @author András Dobreff
 */
public class InstantHandler extends AbstractHandler implements IHandler {
	
	/**
	 * Collects the parameters and calls the {@link PapyrusVisualizer#run()}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
		
		IFile file = (IFile) structured.getFirstElement();
		IProject project = file.getProject();

		
		PapyrusVisualizer ma = new PapyrusVisualizer(project.getName(), getFileNameWithOutExtension(file), file.getRawLocationURI().toString());
		ma.run();
		return null;
	}
	
	/**
	 * Gets the FileName without extension
	 * @param file - The File
	 * @return - Filname without extension
	 */
	private String getFileNameWithOutExtension(IFile file){
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}

}
