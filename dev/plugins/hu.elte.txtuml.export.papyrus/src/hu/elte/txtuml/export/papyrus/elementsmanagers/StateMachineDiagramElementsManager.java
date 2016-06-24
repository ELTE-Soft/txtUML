package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.export.papyrus.api.elementcreators.StateMachineDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.elementproviders.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.StateMachineDiagramElementsArranger;

/**
 * An abstract class for adding/removing elements to StateMachineDiagrams.
 */
public class StateMachineDiagramElementsManager extends AbstractDiagramElementsManager {

	protected StateMachineDiagramNotationManager notationManager;
	protected StateMachineDiagramElementsProvider elementsProvider;
	protected StateMachineDiagramElementsArranger arranger;

	/**
	 * The Constructor
	 * 
	 * @param modelManager
	 *            - The ModelManager which serves the model elements
	 * @param diagramEditPart
	 *            - The DiagramEditPart of the diagram which is to be handled
	 */
	public StateMachineDiagramElementsManager(Diagram diagram, StateMachineDiagramElementsProvider provider,
			TransactionalEditingDomain domain, StateMachineDiagramElementsArranger arranger, IProgressMonitor monitor) {
		super(diagram);
		this.notationManager = new StateMachineDiagramNotationManager(diagram, domain); // TODO:
																						// Consider
																						// DI
		this.arranger = arranger;

		try {
			this.arranger.arrange(monitor);
		} catch (ArrangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.elementsProvider = provider;
		this.monitor = monitor;
	}

	public StateMachineDiagramElementsManager(Diagram diagram, StateMachineDiagramElementsProvider provider,
			TransactionalEditingDomain domain, StateMachineDiagramElementsArranger arranger) {
		this(diagram, provider, domain, arranger, new NullProgressMonitor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.
	 * AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram() {
		Collection<Region> regions = this.elementsProvider.getMainRegions();
		addSubelementsRecursively(regions);
	}

	private void addSubelementsRecursively(Collection<Region> regions) {
		regions.stream().forEach(region -> {
			this.elementsProvider.getStatesForRegion(region).forEach(state -> {
				this.notationManager.createStateForRegion(region, state, this.monitor);
				addSubelementsRecursively(this.elementsProvider.getRegionsOfState(state));
			});
			this.elementsProvider.getInitialStatesForRegion(region).forEach(initialState -> {
				this.notationManager.createInitialStateForRegion(region, initialState, monitor);
			});
			this.elementsProvider.getTransitionsForRegion(region).forEach(transition -> {
				
				Vertex source = transition.getSource();
				Vertex target = transition.getTarget();
				
				this.notationManager.createTransitionForRegion(region, source, target, transition,
						this.arranger.getRouteForConnection(transition),
						this.arranger.getSourceAnchorForConnection(transition),
						this.arranger.getTargetAnchorForConnection(transition), this.monitor);
			});
		});
	}

}
