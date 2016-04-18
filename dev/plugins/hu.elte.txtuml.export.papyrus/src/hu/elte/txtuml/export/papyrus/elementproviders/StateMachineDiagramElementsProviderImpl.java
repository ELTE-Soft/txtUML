package hu.elte.txtuml.export.papyrus.elementproviders;

import java.util.Collection;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;

import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsProviderImpl implements StateMachineDiagramElementsProvider{

	private DiagramExportationReport report;

	public StateMachineDiagramElementsProviderImpl(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		cacheNodes(mapper);
		cacheConnections(mapper);
	}
	
	private void cacheConnections(TxtUMLElementsMapper mapper) {
		// TODO Auto-generated method stub
		
	}

	private void cacheNodes(TxtUMLElementsMapper mapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<State> getStatesForRegion(Region region) {
		// TODO Auto-generated method stub
		return null;
	}

}
