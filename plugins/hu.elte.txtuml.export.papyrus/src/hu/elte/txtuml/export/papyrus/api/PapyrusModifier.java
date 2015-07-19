package hu.elte.txtuml.export.papyrus.api;

import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;
import hu.elte.txtuml.export.papyrus.utils.EditPartFinder;
import hu.elte.txtuml.export.papyrus.utils.EditorOpener;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;

/**
 *
 * @author András Dobreff
 */
public class PapyrusModifier {

	private PapyrusModelCreator papyrusModelCreator;
	private IMultiDiagramEditor editor;
	private DiagramManager diagramManager;
	
	/**
	 * Instantiates the Papyrus model modifier class with a project and model 
	 * @param projectName
	 * @param modelName
	 */
	public PapyrusModifier(String projectName, String modelName){
		papyrusModelCreator = new PapyrusModelCreator(projectName+"/"+modelName);

		if(papyrusModelCreator.diExists()){
			editor = EditorOpener.openPapyrusEditor(papyrusModelCreator.getDi());
			diagramManager = new DiagramManager(editor);
		}
	}
	
	/**
	 * Returns the {@link Model} handled by the PapyrusModel 
	 * @return the uml Model
	 */
	public Model getUmlModel(){
		return papyrusModelCreator.getUmlModel();
	}
	
	/**
	 * Returns the active Edit Part
	 * @return The active DiagramEditPart 
	 */
	public DiagramEditPart getActiveDiagramEditPart(){
		IEditorPart ied = editor.getActiveEditor();
		if(ied instanceof IDiagramWorkbenchPart){
			return ((IDiagramWorkbenchPart) ied).getDiagramEditPart();
		}else{
			return null;
		}
	}
	
	/**
	 * Returns every diagram of the Papyrus model
	 * @return List of Diagrams
	 */
	public List<Diagram> getDiagrams(){
		return diagramManager.getDiagrams();
	}
	
	/**
	 * Opens the given diagram
	 * @param diag
	 */
	public void openDiagram(Diagram diag){
		diagramManager.openDiagram(diag);
	}
	
	/**
	 * Opens the diagram and returns it's EditPart
	 * @param diagram
	 * @return The DiagramEditPart
	 */
	public DiagramEditPart getDiagramEditPart(Diagram diagram){
		diagramManager.openDiagram(diagram);
		DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
		return diagep;
	}
	
	/**
	 * Searches the specified EditPart on the active diagram which holds reference to the Model Element
	 * @param element
	 * @return The EditPart which holds reference to the Model Element or <i>null</i> if it's on the diagram 
	 */
	public EditPart getEditPartOfElement(Element element){
		return EditPartFinder.getEditPartofElement(diagramManager.getActiveDiagramEditPart(), element);
	}
	
	/**
	 * Saves the model
	 */
	public void save(){
		editor.doSave(new NullProgressMonitor());
	}
}
