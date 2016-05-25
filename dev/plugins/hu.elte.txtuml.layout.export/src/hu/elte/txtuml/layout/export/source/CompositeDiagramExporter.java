package hu.elte.txtuml.layout.export.source;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

public class CompositeDiagramExporter  extends AbstractSourceExporter {

	@Override
	public DiagramType getType() {
		return DiagramType.Composite;
	}

	@Override
	public boolean isNode(Class<?> cls) {
		if(AssociationEnd.class.isAssignableFrom(cls)) {
			Class<?> enclosing = cls.getEnclosingClass();
			return Composition.class.isAssignableFrom(enclosing);
		}
		return false;
	}

	@Override
	public boolean isLink(Class<?> cls) {
		// TODO: connectors
		return false;
	}

	@Override
	public Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link) throws ElementExportationException {
		// TODO identify end points of connectors
		return null;
	}

	@Override
	public void exportImpliedLinks(ModelId modelId, ElementExporter elementExporter) {
		// TODO
		
	}

}
