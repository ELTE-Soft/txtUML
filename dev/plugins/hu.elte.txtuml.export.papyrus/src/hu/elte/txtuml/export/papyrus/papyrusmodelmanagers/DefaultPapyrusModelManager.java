package hu.elte.txtuml.export.papyrus.papyrusmodelmanagers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.uml.diagram.activity.CreateActivityDiagramCommand;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.statemachine.CreateStateMachineDiagramCommand;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.ActivityDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.ClassDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout.StateMachineDiagramElementsGmfArranger;
import hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ActivityDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.StateMachineDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

/**
 *	The Default representation of Papyrus Model manager
 */
public class DefaultPapyrusModelManager extends AbstractPapyrusModelManager {

	public DefaultPapyrusModelManager(ServicesRegistry registry) {
		super(registry);
	}
	

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager#createDiagrams(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void createDiagrams(IProgressMonitor monitor){
		monitor.beginTask("Generating empty diagrams", 100);
		monitor.subTask("Creating empty diagrams...");
		
		if(PreferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_PREF)){
			List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Model.class, Package.class));
			diagramManager.createDiagrams(packages, new CreateClassDiagramCommand(), this.domain);
		}
		
		if(PreferencesManager.getBoolean(PreferencesManager.ACTIVITY_DIAGRAM_PREF)){
			List<Element> activities = modelManager.getElementsOfTypes(Arrays.asList(Activity.class));
			diagramManager.createDiagrams(activities, new CreateActivityDiagramCommand(), this.domain);
		}
		
		if(PreferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_PREF)){
			List<Element> statemachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
			diagramManager.createDiagrams(statemachines, new CreateStateMachineDiagramCommand(), this.domain);
		}
		monitor.worked(100);
	}

	/**
	 * This implementation does not uses any controlling object
	 */
	@Override
	public void setLayoutController(Object layoutcontroller) {}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager#arrangeElementsOfDiagram(org.eclipse.gmf.runtime.notation.Diagram, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void arrangeElementsOfDiagram(Diagram diagram, IProgressMonitor monitor) throws ArrangeException{
		IDiagramElementsArranger diagramElementsArranger;
		DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
		if(diagram.getType().equals("PapyrusUMLClassDiagram")){					
			diagramElementsArranger = new ClassDiagramElementsGmfArranger(diagep);
		}else if(diagram.getType().equals("PapyrusUMLActivityDiagram")){
			diagramElementsArranger = new ActivityDiagramElementsGmfArranger(diagep);
		}else if(diagram.getType().equals("PapyrusUMLStateMachineDiagram")){
			diagramElementsArranger = new StateMachineDiagramElementsGmfArranger(diagep);
		}else{
			return;
		}
		
		diagramElementsArranger.arrange(monitor);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager#addElementsToDiagram(org.eclipse.gmf.runtime.notation.Diagram, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void addElementsToDiagram(Diagram diagram, IProgressMonitor monitor) {
		
		Element container = diagramManager.getDiagramContainer(diagram);
		AbstractDiagramElementsManager diagramElementsManager;
		if(diagram.getType().equals("PapyrusUMLClassDiagram")){					
			diagramElementsManager = new ClassDiagramElementsManager(diagram, null, this.domain);
		}else if(diagram.getType().equals("PapyrusUMLActivityDiagram")){
			diagramElementsManager = new ActivityDiagramElementsManager(diagram, this.domain);
		}else if(diagram.getType().equals("PapyrusUMLStateMachineDiagram")){
			diagramElementsManager = new StateMachineDiagramElementsManager(diagram, this.domain);
		}else{
			return;
		}
		
		List<Element> baseElements = modelManager.getAllChildrenOfPackage(container);
		diagramElementsManager.addElementsToDiagram(baseElements);
	}	

}
