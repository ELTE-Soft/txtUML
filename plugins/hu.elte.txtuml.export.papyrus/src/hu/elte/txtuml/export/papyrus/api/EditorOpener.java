package hu.elte.txtuml.export.papyrus.api;

import hu.elte.txtuml.export.utils.Dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

@SuppressWarnings("javadoc")
public class EditorOpener {

	private static final String PapyrusEditorId = "org.eclipse.papyrus.infra.core.papyrusEditor";
	
	/**
	 * Opens an editor for the file
	 * @param diFile A file in the project
	 * @return The EditorPart of the editor
	 * @throws PartInitException
	 */
	public static final IMultiDiagramEditor openPapyrusEditor(final IFile diFile){
			IMultiDiagramEditor ed = null;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(page != null) {
				try {
					IEditorInput editorInput = new FileEditorInput(diFile);
					ed = (IMultiDiagramEditor) IDE.openEditor(page, editorInput, PapyrusEditorId, true);
				} catch (PartInitException e) {
					Dialogs.errorMsgb(null, null, e);
				}
			}
			return ed;
	}
}
