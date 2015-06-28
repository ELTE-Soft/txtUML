package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.ClassDiagramElementsTxtUmlArranger;
import hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;

/**
 * Controls the Papyrus Model
 *
 * @author András Dobreff
 */
public class PapyrusTxtUMLModelManager extends AbstractPapyrusModelManager {

	private TxtUMLLayoutDescriptor descriptor;
	
	/**
	 * @param editor
	 * @param descriptor 
	 * @param report
	 * @throws ServiceException
	 * @throws NotFoundException
	 */
	public PapyrusTxtUMLModelManager(IMultiDiagramEditor editor, TxtUMLLayoutDescriptor descriptor) 
			throws ServiceException, NotFoundException {
		super(editor);
		this.descriptor = descriptor;
	}

	@Override
	protected void addElementsToDiagrams() throws ServiceException {
		
		List<Diagram> diags =  diagramManager.getDiagrams();
		
		for(Diagram diag : diags){
			Diagram diagram = diag;
			
			diagramManager.openDiagram(diagram);
			DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
			AbstractDiagramElementsManager diagramElementsManager;
			IDiagramElementsArranger diagramElementsArranger;  
			
			
			List<Element> baseElements = new ArrayList<Element>();
			
			TxtUMLElementsFinder finder = new TxtUMLElementsFinder(this.modelManager, this.descriptor); 
			baseElements.addAll(finder.getNodes());
			baseElements.addAll(finder.getConnections());
			
			if(diagram.getType().equals("PapyrusUMLClassDiagram")){					
				diagramElementsManager = new ClassDiagramElementsManager(modelManager, diagep);
				diagramElementsArranger = new ClassDiagramElementsTxtUmlArranger(diagep, finder);
			}else{
				continue;
			}
			
			diagramElementsManager.addElementsToDiagram(baseElements);	
			diagramElementsArranger.arrange();
		}
	}

	@Override
	protected void createDiagrams() {
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_PREF)){
			List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
			diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		}
	}

}
