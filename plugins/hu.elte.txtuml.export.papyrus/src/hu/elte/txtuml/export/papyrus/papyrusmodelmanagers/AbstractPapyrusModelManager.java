package hu.elte.txtuml.export.papyrus.papyrusmodelmanagers;

import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
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
	protected UMLModelManager modelManager;
	
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
	 */
	public AbstractPapyrusModelManager(IMultiDiagramEditor editor){
		this.preferencesManager = new PreferencesManager();
		this.modelManager = new UMLModelManager(editor);
		this.diagramManager = new DiagramManager(editor);
		this.editor = editor;
	}

	/**
	 * Creates the diagrams and adds the elements to them
	 */
	public void createAndFillDiagrams(){
		createDiagrams();	
		addElementsToDiagrams();
		this.editor.doSave(new NullProgressMonitor());
	}

	/**
	 * Adds the elements to the diagrams
	 */
	protected abstract void addElementsToDiagrams();

	/**
	 * Creates the Papyrus Diagrams for every suitable element of the Model
	 */
	protected abstract void createDiagrams();
}
