package hu.elte.txtuml.export.papyrus.elementproviders;

import java.util.Collection;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;

public interface StateMachineDiagramElementsProvider {

	Collection<State> getStatesForRegion(Region region);
	
	Collection<Pseudostate> getInitialStatesForRegion(Region region);
	
	Collection<Region> getRegionsOfState(State state);

	Collection<Region> getMainRegions();
	
	//State , CompositeState?, PseudoState, FinalState, Choice, Merge etc.
}
