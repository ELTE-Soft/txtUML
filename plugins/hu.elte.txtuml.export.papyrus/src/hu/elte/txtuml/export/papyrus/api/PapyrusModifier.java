package hu.elte.txtuml.export.papyrus.api;

import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Element;

/**
 *
 * @author András Dobreff
 */
public class PapyrusModifier {

	private PapyrusModelCreator papyrusModelCreator;
	private IMultiDiagramEditor editor;
	
	/**
	 * Instantiates the Papyrus model modifier class with a project and model 
	 * @param projectName
	 * @param modelName
	 */
	public PapyrusModifier(String projectName, String modelName){
		papyrusModelCreator = new PapyrusModelCreator(projectName+"/"+modelName);

		if(papyrusModelCreator.diExists()){
			editor = EditorOpener.openPapyrusEditor(papyrusModelCreator.getDi());
		}
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
	
	public List<Diagram> getDiagrams(){
		//TODO: Implement
		throw new UnsupportedOperationException("This operaation is not supported yet!");
	}
	
	public DiagramEditPart getDiagramEditPart(Diagram diagram){
		//TODO: Implement
		throw new UnsupportedOperationException("This operaation is not supported yet!");
	}
	
	public EditPart getEditPartOfElementOnActiveDiagram(Element element){
		//TODO: Implement
		throw new UnsupportedOperationException("This operaation is not supported yet!");
	}
	
	/**
	 * Saves the model
	 */
	public void save(){
		editor.doSave(new NullProgressMonitor());
	}
}
