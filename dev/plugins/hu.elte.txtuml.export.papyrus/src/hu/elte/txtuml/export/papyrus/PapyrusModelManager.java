package hu.elte.txtuml.export.papyrus;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.core.resource.BadStateException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.statemachine.CreateStateMachineDiagramCommand;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.papyrus.elementproviders.ClassDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.elementproviders.ClassDiagramElementsProviderImpl;
import hu.elte.txtuml.export.papyrus.elementproviders.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.elementproviders.StateMachineDiagramElementsProviderImpl;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ClassDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.StateMachineDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.elementsmanagers.StateMachineDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.utils.Pair;

/**
 * Controls the Papyrus Model
 */
public class PapyrusModelManager {

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

	private TxtUMLLayoutDescriptor descriptor;

	private TxtUMLElementsMapper mapper;

	/**
	 * The Constructors
	 * 
	 * @param editor
	 *            - The Editor to which the PapyrusModelManager will be attached
	 * @param model
	 *            - The Uml Model manager
	 */
	public PapyrusModelManager(ServicesRegistry registry) {
		try {
			this.modelSet = registry.getService(ModelSet.class);
			this.domain = ServiceUtils.getInstance().getTransactionalEditingDomain(registry);
			this.model = (UmlModel) this.modelSet.getModel(UmlModel.MODEL_ID);
			this.modelSet.loadModel(UmlModel.MODEL_ID);
			this.modelManager = new UMLModelManager(model);
			this.diagramManager = new DiagramManager(registry);
		} catch (ServiceException | BadStateException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Creates the diagrams and adds the elements to them
	 * 
	 * @param monitor
	 *            - The monitor that listens the progress
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
	 * 
	 * @param monitor
	 */
	protected void addElementsToDiagrams(IProgressMonitor monitor) {

		List<Diagram> diags = diagramManager.getDiagrams();
		int diagNum = diags.size();
		monitor.beginTask("Filling diagrams", diagNum * 2);

		for (int i = 0; i < diagNum * 2; i = i + 2) {
			Diagram diagram = diags.get(i / 2);
			monitor.subTask("Filling diagrams " + (i + 2) / 2 + "/" + diagNum);
			addElementsToDiagram(diagram, monitor);
			monitor.worked(1);
		}
	}

	/**
	 * Adds the suitable elements to the diagram
	 * 
	 * @param diagram
	 *            - The diagram
	 * @param monitor
	 *            - The progress monitor
	 */
	protected void addElementsToDiagram(Diagram diagram, IProgressMonitor monitor) {
		AbstractDiagramElementsManager diagramElementsManager;

		DiagramExportationReport report = this.descriptor.getReportByDiagramName(diagram.getName());

		if (diagram.getType().equals(diagramType_CD)) {
			ClassDiagramElementsProvider provider = new ClassDiagramElementsProviderImpl(report, this.mapper);
			ClassDiagramElementsArranger arranger = new ClassDiagramElementsArranger(report, this.mapper);
			diagramElementsManager = new ClassDiagramElementsManager(diagram, provider, this.domain, arranger, monitor);
		} else if (diagram.getType().equals(diagramType_SMD)) {
			StateMachineDiagramElementsProvider provider = new StateMachineDiagramElementsProviderImpl(report,
					this.mapper);
			StateMachineDiagramElementsArranger arranger = new StateMachineDiagramElementsArranger(report, this.mapper);
			diagramElementsManager = new StateMachineDiagramElementsManager(diagram, provider, this.domain, arranger,
					monitor);
		} else {
			return;
		}

		diagramElementsManager.addElementsToDiagram();
	}

	/**
	 * Creates the diagrams
	 * 
	 * @param monitor
	 *            - The progress monitor
	 */
	protected void createDiagrams(IProgressMonitor monitor) {
		monitor.beginTask("Generating empty diagrams", 100);
		monitor.subTask("Creating empty diagrams...");

		List<Pair<DiagramExportationReport, Element>> diagramRoots = mapper
				.getDiagramRootsWithDiagramNames(this.descriptor);

		ICreationCommand cmd;

		for (Pair<DiagramExportationReport, Element> diagramRoot : diagramRoots) {
			DiagramExportationReport report = diagramRoot.getFirst();
			Element root;

			if (report.getType() == DiagramType.Class) {
				root = diagramRoot.getSecond();
				cmd = new CreateClassDiagramCommand();
			} else if (report.getType() == DiagramType.StateMachine) {
				BehavioredClassifier referencedElement = (BehavioredClassifier) diagramRoot.getSecond();
				root = referencedElement.getClassifierBehavior();
				cmd = new CreateStateMachineDiagramCommand();
			} else {
				continue;
			}

			diagramRoot.getSecond();
			diagramManager.createDiagram(root, report.getReferencedElementName(), cmd, this.domain);
		}

		monitor.worked(100);
	}

	/**
	 * Sets the layout controlling object. This object will affect the suitable
	 * elements and arrage of the diagram
	 * 
	 * @param layoutcontroller
	 *            - The controller object
	 */
	public void setLayoutController(Object layoutcontroller) {
		TxtUMLLayoutDescriptor descriptor = (TxtUMLLayoutDescriptor) layoutcontroller;
		this.descriptor = descriptor;
		this.mapper = new TxtUMLElementsMapper(this.model.getResource(), descriptor);
	}
}
