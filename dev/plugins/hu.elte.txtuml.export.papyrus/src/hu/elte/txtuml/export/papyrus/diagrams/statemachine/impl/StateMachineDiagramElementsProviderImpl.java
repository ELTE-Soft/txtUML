package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.diagrams.common.layout.IDiagramElementsMapper;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsProviderImpl implements StateMachineDiagramElementsProvider {

	private StateMachine mainElement;
	private DiagramExportationReport report;
	private Collection<Element> nodes;
	private Collection<Transition> connections;

	public StateMachineDiagramElementsProviderImpl(DiagramExportationReport report, IDiagramElementsMapper mapper) {
		this.report = report;
		cacheNodes(mapper);
		cacheConnections(mapper);
		Class clazz = (Class) mapper.findNode(this.report.getReferencedElementName());

		this.mainElement = (StateMachine) clazz.getClassifierBehavior();
	}

	private void cacheConnections(IDiagramElementsMapper mapper) {
		this.connections = mapper.getConnections().stream().filter(c -> c instanceof Transition)
				.map(c -> (Transition) c).collect(Collectors.toList());
	}

	private void cacheNodes(IDiagramElementsMapper mapper) {
		this.nodes = mapper.getNodes();
	}

	@Override
	public Collection<State> getStatesForRegion(Region region) {
		List<State> result = new ArrayList<>();

		result = region.getSubvertices().stream().filter(e -> (e instanceof State) && this.nodes.contains(e))
				.map(e -> (State) e).collect(Collectors.toList());
		return result;
	}

	@Override
	public Collection<Region> getMainRegions() {
		return mainElement.getRegions();
	}

	@Override
	public StateMachine getMainElement() {
		return mainElement;
	}

	@Override
	public Collection<Region> getRegionsOfState(State state) {
		return state.getRegions();
	}

	@Override
	public Collection<Pseudostate> getInitialStatesForRegion(Region region) {
		List<Pseudostate> result = new ArrayList<>();
		result = region.getOwnedElements().stream().filter(e -> (e instanceof Pseudostate) && this.nodes.contains(e))
				.map(e -> (Pseudostate) e).collect(Collectors.toList());
		return result;
	}

	@Override
	public Collection<Transition> getTransitionsForRegion(Region region) {
		List<Transition> result = new ArrayList<>();
		result = region.getOwnedElements().stream()
				.filter(e -> (e instanceof Transition) && this.connections.contains(e)).map(e -> (Transition) e)
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public Collection<Element> getElementsOfRegion(Region region) {
		List<Element> result = new ArrayList<>();
		result = region.getOwnedElements().stream().filter(e -> this.nodes.contains(e)).collect(Collectors.toList());
		return result;
	}

}
