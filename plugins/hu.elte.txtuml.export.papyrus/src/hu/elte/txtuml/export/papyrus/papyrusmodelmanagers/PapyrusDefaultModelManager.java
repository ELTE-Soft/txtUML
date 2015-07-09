package hu.elte.txtuml.export.papyrus.papyrusmodelmanagers;

import hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.ActivityDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.ClassDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.StateMachineDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ActivityDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.StateMachineDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.utils.Dialogs;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.uml.diagram.activity.CreateActivityDiagramCommand;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.statemachine.CreateStateMachineDiagramCommand;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.StateMachine;

/**
 * Controls the Papyrus Model
 *
 * @author András Dobreff
 */
public class PapyrusDefaultModelManager extends AbstractPapyrusModelManager{
	
	/**
	 * The Constructor
	 * @param editor
	 */
	public PapyrusDefaultModelManager(IMultiDiagramEditor editor){
		super(editor);
	}

	@Override
	protected void createDiagrams(IProgressMonitor monitor){
		monitor.beginTask("Generating empty diagrams", 100);
		monitor.subTask("Creating empty diagrams...");
		
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_PREF)){
			List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Model.class, Package.class));
			diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		}
	
		if(preferencesManager.getBoolean(PreferencesManager.ACTIVITY_DIAGRAM_PREF)){
			List<Element> activities = modelManager.getElementsOfTypes(Arrays.asList(Activity.class));
			diagramManager.createDiagrams(activities, new CreateActivityDiagramCommand());
		}
		
		if(preferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_PREF)){
			List<Element> statemachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
			diagramManager.createDiagrams(statemachines, new CreateStateMachineDiagramCommand());
		}
		monitor.worked(100);
	}

	/**
	 * Adds the elements to the diagrams
	 */
	@Override
	protected void addElementsToDiagrams(IProgressMonitor monitor){
		
		List<Diagram> diags =  diagramManager.getDiagrams();
		int diagNum = diags.size();
		monitor.beginTask("Filling diagrams", diagNum*2);
		
		for(int i=0; i<diagNum*2; i=i+2){
			monitor.subTask("Filling diagrams "+(i+2)/2+"/"+diagNum);
			Diagram diagram = diags.get(i/2);
			
			Element container = diagramManager.getDiagramContainer(diagram);
			diagramManager.openDiagram(diagram);
			DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
			AbstractDiagramElementsManager diagramElementsManager;
			IDiagramElementsArranger diagramElementsArranger;  
			
			List<Element> baseElements = modelManager.getAllChildrenOfPackage(container);
			
			if(diagram.getType().equals("PapyrusUMLClassDiagram")){					
				diagramElementsManager = new ClassDiagramElementsManager(modelManager, diagep);
				diagramElementsArranger = new ClassDiagramElementsGmfArranger(diagep);
			}else if(diagram.getType().equals("PapyrusUMLActivityDiagram")){
				diagramElementsManager = new ActivityDiagramElementsManager(modelManager, diagep);
				diagramElementsArranger = new ActivityDiagramElementsGmfArranger(diagep);
			}else if(diagram.getType().equals("PapyrusUMLStateMachineDiagram")){
				diagramElementsManager = new StateMachineDiagramElementsManager(modelManager, diagep);
				diagramElementsArranger = new StateMachineDiagramElementsGmfArranger(diagep);
			}else{
				continue;
			}
			
			diagramElementsManager.addElementsToDiagram(baseElements);	
			monitor.worked(1);
			try{
				diagramElementsArranger.arrange(new SubProgressMonitor(monitor, 1));
			}catch(Throwable e){
				Dialogs.errorMsgb("Arrange error", e.toString(), e);
			}
			
		}
	}
}
