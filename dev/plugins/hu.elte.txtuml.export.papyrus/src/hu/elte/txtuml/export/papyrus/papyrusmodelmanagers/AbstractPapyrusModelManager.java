package hu.elte.txtuml.export.papyrus.papyrusmodelmanagers;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.resource.BadStateException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.utils.eclipse.Dialogs;

/**
 * Controls the Papyrus Model
 */
public abstract class AbstractPapyrusModelManager {

	
	/**
	 * TODO add doc
	 */
	protected final static String diagramType_CD = "PapyrusUMLClassDiagram";
	/**
	 * TODO add doc
	 */
	protected final static String diagramType_SMD = "PapyrusUMLStateMachineDiagram";
	/**
	 * TODO add doc
	 */
	protected final static String diagramType_AD = "PapyrusUMLActivityDiagram";
	
	/**
	 * The DiagramManager controls the diagrams
	 */
	protected DiagramManager diagramManager;
	
	/**
	 * The ModelManager controls the model elements
	 */
	protected UMLModelManager modelManager;
	
	
	protected ModelSet modelSet;
	
	
	protected TransactionalEditingDomain domain;
	
	/**
	 * The resource were the elements are stored
	 */
	protected UmlModel model;

	
	/**
	 * The Constructor
	 * @param editor - The Editor to which the PapyrusModelManager will be attached
	 * @param model - The Uml Model manager
	 */
	public AbstractPapyrusModelManager(ServicesRegistry registry){
		try{
			this.modelSet = registry.getService(ModelSet.class);
			this.domain = ServiceUtils.getInstance()
					.getTransactionalEditingDomain(registry);
			this.model = (UmlModel)this.modelSet.getModel(UmlModel.MODEL_ID);
			this.modelSet.loadModel(UmlModel.MODEL_ID);
			this.modelManager = new UMLModelManager(model);
			this.diagramManager = new DiagramManager(registry);
		}catch(ServiceException | BadStateException e){
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Creates the diagrams and adds the elements to them
	 * @param monitor - The monitor that listens the progress
	 */
	public void createAndFillDiagrams(IProgressMonitor monitor) {
		monitor.beginTask("Generating Diagrams", 100);
		createDiagrams(new SubProgressMonitor(monitor, 20));
		addElementsToDiagrams(new SubProgressMonitor(monitor, 80));
		try {
			this.modelSet.save(new NullProgressMonitor());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the elements to the diagrams
	 * @param monitor
	 */
	protected void addElementsToDiagrams(IProgressMonitor monitor){
		
		List<Diagram> diags =  diagramManager.getDiagrams();
		int diagNum = diags.size();
		monitor.beginTask("Filling diagrams", diagNum*2);
		
		for(int i=0; i<diagNum*2; i=i+2){
			Diagram diagram = diags.get(i/2);
			diagramManager.openDiagram(diagram);
			monitor.subTask("Filling diagrams "+(i+2)/2+"/"+diagNum);
			addElementsToDiagram(diagram, monitor);
			monitor.worked(1);
			
			try{
			//	arrangeElementsOfDiagram(diagram, monitor);
			}catch(Throwable e){
				Dialogs.errorMsgb("Arrange error",
						"Error occured during arrangement of diagram '" +diagram.getName()+"'.", e);
			}
		}
	}
	
	/**
	 * Arranges the diagram elements 
	 * @param diagram - The diagram 
	 * @param monitor - The progress monitor
	 * @throws ArrangeException
	 */
	protected abstract void arrangeElementsOfDiagram(Diagram diagram, IProgressMonitor monitor) throws ArrangeException;
	
	/**
	 * Adds the suitable elements to the diagram
	 * @param diagram - The diagram 
	 * @param monitor - The progress monitor
	 */
	protected abstract void addElementsToDiagram(Diagram diagram, IProgressMonitor monitor);
	
	/**
	 * Creates the diagrams
	 * @param monitor - The progress monitor
	 */
	protected abstract void createDiagrams(IProgressMonitor monitor);
	
	/**
	 * Sets the layout controlling object. This object will affect the
	 * suitable elements and arrage of the diagram
	 * @param layoutcontroller - The controller object
	 */
	public abstract void setLayoutController(Object layoutcontroller);
}
