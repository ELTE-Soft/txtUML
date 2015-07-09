package hu.elte.txtuml.export.papyrus.papyrusmodelmanagers;

import hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.ClassDiagramElementsTxtUmlArranger;
import hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.utils.Dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
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
	 */
	public PapyrusTxtUMLModelManager(IMultiDiagramEditor editor, TxtUMLLayoutDescriptor descriptor){
		super(editor);
		this.descriptor = descriptor;
	}

	@Override
	protected void addElementsToDiagrams(){
		
		List<Diagram> diags =  diagramManager.getDiagrams();
		
		for(Diagram diag : diags){
			Diagram diagram = diag;
			
			diagramManager.openDiagram(diagram);
			DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
			AbstractDiagramElementsManager diagramElementsManager;
			IDiagramElementsArranger diagramElementsArranger;  
			
			
			List<Element> baseElements = new ArrayList<Element>();
			
			TxtUMLElementsRegistry txtumlregistry = new TxtUMLElementsRegistry(this.modelManager, this.descriptor); 
			baseElements.addAll(txtumlregistry.getNodes());
			baseElements.addAll(txtumlregistry.getConnections());
			
			if(diagram.getType().equals("PapyrusUMLClassDiagram")){					
				diagramElementsManager = new ClassDiagramElementsManager(modelManager, diagep);
				diagramElementsArranger = new ClassDiagramElementsTxtUmlArranger(diagep, txtumlregistry);
			}else{
				continue;
			}
			
			diagramElementsManager.addElementsToDiagram(baseElements);	
			try {
				diagramElementsArranger.arrange();
			} catch (Throwable e) {
				Dialogs.errorMsgb("Arrange error", e.toString(), e);
			}
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
