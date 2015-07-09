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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
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
	protected void addElementsToDiagrams(IProgressMonitor monitor){

		List<Diagram> diags =  diagramManager.getDiagrams();
		int diagNum = diags.size();
		monitor.beginTask("Filling diagrams", diagNum*2);
		
		for(int i=0; i<diagNum*2; i=i+2){
			monitor.subTask("Filling diagrams "+(i+2)/2+"/"+diagNum);
			Diagram diagram = diags.get(i/2);
			
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
			monitor.worked(1);
			try {
				diagramElementsArranger.arrange(new SubProgressMonitor(monitor, 1));
			} catch (Throwable e) {
				Dialogs.errorMsgb("Arrange error", e.toString(), e);
			}
			
		}
	}

	@Override
	protected void createDiagrams(IProgressMonitor monitor) {
		monitor.beginTask("Generating empty diagrams", 100);
		monitor.subTask("Creating empty diagrams...");
		
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_PREF)){
			List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
			diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		}
		monitor.worked(100);
	}

}
