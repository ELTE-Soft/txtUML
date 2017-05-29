package hu.elte.txtuml.export.papyrus;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
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
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.arrange.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsMapper;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsProviderImpl;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramNotationManagerImpl;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsMapper;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsProviderImpl;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramNotationManagerImpl;
import hu.elte.txtuml.export.papyrus.layout.TxtUMLElementsMapper;
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
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.setTaskName("Generating Diagrams");
		createDiagrams(subMonitor.newChild(20));
		if(monitor.isCanceled()) return;
		addElementsToDiagrams(subMonitor.newChild(80));

		try {
			this.modelSet.save(new NullProgressMonitor());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			monitor.done();
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
		SubMonitor loopProgress = SubMonitor.convert(monitor, diagNum);

		int i = 1;
		for (Diagram diagram : diags) {
			if(monitor.isCanceled()) return;
			loopProgress.setTaskName("Filling diagrams " + i + "/" + diagNum);
			addElementsToDiagram(diagram, loopProgress.newChild(1));
			i++;
		}
		loopProgress.done();
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
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		if (diagram.getType().equals(diagramType_CD)) {
			ClassDiagramElementsMapper mapper = (ClassDiagramElementsMapper) this.mapper.getMapperForReport(report);

			ClassDiagramElementsProvider provider = new ClassDiagramElementsProviderImpl(mapper);
			AbstractDiagramElementsArranger arranger = new ClassDiagramElementsArranger(report, mapper);
			ClassDiagramNotationManager notation = new ClassDiagramNotationManagerImpl(diagram, this.domain);
			diagramElementsManager = new ClassDiagramElementsManager(diagram, provider, notation, arranger,
					subMonitor.newChild(100));
		} else if (diagram.getType().equals(diagramType_SMD)) {
			StateMachineDiagramElementsMapper mapper = (StateMachineDiagramElementsMapper) this.mapper
					.getMapperForReport(report);

			StateMachineDiagramElementsProvider provider = new StateMachineDiagramElementsProviderImpl(report, mapper);
			StateMachineDiagramElementsArranger arranger = new StateMachineDiagramElementsArranger(report, mapper);
			StateMachineDiagramNotationManager notation = new StateMachineDiagramNotationManagerImpl(diagram,
					this.domain);
			diagramElementsManager = new StateMachineDiagramElementsManager(diagram, provider, notation, arranger,
					subMonitor.newChild(100));
		} else {
			return;
		}

		diagramElementsManager.addElementsToDiagram();
		subMonitor.done();
	}

	/**
	 * Creates the diagrams
	 * 
	 * @param monitor
	 *            - The progress monitor
	 */
	protected void createDiagrams(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor);
		subMonitor.setTaskName("Generating empty diagrams...");

		List<Pair<DiagramExportationReport, Element>> diagramRoots = mapper
				.getDiagramRootsWithDiagramNames(this.descriptor);

		ICreationCommand cmd;
		int diagNum = diagramRoots.size();
		subMonitor.setWorkRemaining(diagNum);
		int i = 1;
		for (Pair<DiagramExportationReport, Element> diagramRoot : diagramRoots) {

			subMonitor.setTaskName("Creating diagram " + i + "/" + diagNum + "...");

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
			subMonitor.worked(1);
			i++;
		}
		subMonitor.done();
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
