package hu.elte.txtuml.layout.export.diagramexporters;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class StateMachineDiagramExporter extends AbstractSourceExporter {

	@Override
	public DiagramType getType() {
		return DiagramType.StateMachine;
	}

	@Override
	public boolean isNode(Class<?> cls) {
		return Vertex.class.isAssignableFrom(cls);
	}

	@Override
	public boolean isLink(Class<?> cls) {
		return Transition.class.isAssignableFrom(cls);
	}

	@Override
	public Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link)
			throws ElementExportationException {
		From from = link.getAnnotation(From.class);
		To to = link.getAnnotation(To.class);
		if (from == null || to == null) {
			throw new ElementExportationException();
		}
		return Pair.of(from.value(), to.value());
	}

	@Override
	protected List<Class<?>> loadAllLinksFromModel(Class<?> model) {		
		List<Class<?>> links = new ArrayList<>();
		for (Class<?> cls : model.getDeclaredClasses()) {
			if (ModelClass.class.isAssignableFrom(model) ||
					CompositeState.class.isAssignableFrom(model)) {
				links.addAll(loadAllLinksFromModel(cls));
			}
			if (isLink(cls)) {
				links.add(cls);
			}
		}
		return links;
	}
}
