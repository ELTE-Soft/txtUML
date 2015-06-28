package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.ui.IEditorPart;

/**
 * Controls the Papyrus Model
 *
 * @author András Dobreff
 */
public abstract class AbstractPapyrusModelManager {

	/**
	 * The DiagramManager controls the diagrams
	 */
	protected DiagramManager diagramManager;
	
	/**
	 * The ModelManager controls the model elements
	 */
	protected ModelManager modelManager;
	
	/**
	 * The PereferecesManager gives information what the Diagrams and Elements should be seen
	 */
	protected PreferencesManager preferencesManager;
	
	/**
	 * The Editor in which the the visualization is performed
	 */
	protected IEditorPart editor;

	/**
	 * The Constructor
	 * @param editor - The Editor to which the PapyrusModelManager will be attached
	 * @throws ServiceException
	 * @throws NotFoundException
	 */
	public AbstractPapyrusModelManager(IMultiDiagramEditor editor) throws ServiceException, NotFoundException {
		preferencesManager = new PreferencesManager();
		modelManager = new ModelManager(editor);
		diagramManager = new DiagramManager(editor);
		this.editor = editor;
	}

	/**
	 * Creates the diagrams and adds the elements to them
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void createAndFillDiagrams() throws ServiceException {
		createDiagrams();	
		addElementsToDiagrams();
		editor.doSave(new NullProgressMonitor());
	}

	/**
	 * Adds the elements to the diagrams
	 * @throws ServiceException 
	 */
	protected abstract void addElementsToDiagrams() throws ServiceException;

	/**
	 * Creates the Papyrus Diagrams for every suitable element of the Model
	 */
	protected abstract void createDiagrams();
}
