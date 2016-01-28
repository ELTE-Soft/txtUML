package hu.elte.txtuml.layout.export.source;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

public class StateMachineDiagramExporter extends AbstractSourceExporter {

	@Override
	public DiagramType getType() {
		return DiagramType.StateMachine;
	}
	
	/*@Override
	public String getReferencedElementName(NodeMap nodes) {
		NodeInfo nodeinfo = nodes.values().iterator().next();
		return nodeinfo.getElementClass().getEnclosingClass().getCanonicalName();
	}*/

	@Override
	public boolean isNode(Class<?> cls) {
		return Vertex.class.isAssignableFrom(cls);
	}

	@Override
	public boolean isLink(Class<?> cls) {
		return Transition.class.isAssignableFrom(cls);
	}

	@Override
	public Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link) throws ElementExportationException {
		From from = link.getAnnotation(From.class);
		To to = link.getAnnotation(To.class);
		if (from == null || to == null) {
			throw new ElementExportationException();
		}
		return Pair.of(from.value(), to.value());
	}

	@Override
	public void exportImpliedLinks(ModelId modelId, ElementExporter elementExporter) {

		elementExporter.getNodes().keySet().forEach(node -> exportImpliedLinksFromSpecifiedNode(elementExporter, node));

	}

	private void exportImpliedLinksFromSpecifiedNode(ElementExporter elementExporter, Class<?> node) {

		Class<?> parent = node.getEnclosingClass();
		for (Class<?> cls : parent.getDeclaredClasses()) {
			try {
				if (isLink(cls)) {
					Pair<Class<?>, Class<?>> p = getStartAndEndOfLink(cls);

					if (p.getFirst() == node && elementExporter.getNodes().containsKey(p.getSecond())) {
						elementExporter.exportLink(cls);
					}
				}
			} catch (ElementExportationException e) {
				// do nothing (step to next link)
			}
		}

	}
}
