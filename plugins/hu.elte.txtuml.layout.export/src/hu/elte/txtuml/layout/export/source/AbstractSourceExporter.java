package hu.elte.txtuml.layout.export.source;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public abstract class AbstractSourceExporter implements SourceExporter {

	@Override
	public ModelId getModelOf(Class<?> element)
			throws ElementExportationException {
		try {
			Package ownPackage = element.getPackage();
			if (ownPackage.getAnnotation(Model.class) != null) {
				return new ModelIdImpl(ownPackage);
			}
			String ownPackageName = ownPackage.getName();

			return new ModelIdImpl(Stream.of(Package.getPackages())
					.filter(p -> ownPackageName.startsWith(p.getName() + "."))
					.sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
					.findFirst().get());

		} catch (NoSuchElementException e) {
			throw new ElementExportationException();
		}
	}

	@Override
	public void exportImpliedLinks(ModelId modelId,
			ElementExporter elementExporter) {

		if (modelId != null && modelId instanceof ModelIdImpl) {

			// TODO export implied links
			
			// Package topPackage = ((ModelIdImpl) modelId).getPackage();
			// List<Class<?>> links = loadAllLinksFromModel(model);
			//
			// elementExporter.getNodes().forEach( (cls, node) ->
			// exportImpliedLinksFromSpecifiedNode(model, elementExporter,
			// links, node.getElementClass()));

		}

	}

	protected abstract List<Class<?>> loadAllLinksFromModel(Class<?> model);

	protected void exportImpliedLinksFromSpecifiedNode(Class<?> model,
			ElementExporter elementExporter, List<Class<?>> links, Class<?> node) {
		NodeMap nodes = elementExporter.getNodes();
		for (Class<?> link : links) {
			try {
				Pair<Class<?>, Class<?>> p = getStartAndEndOfLink(link);

				// nodes.containsKey(node) should be guaranteed here
				if ((p.getFirst().equals(node) && nodes.containsKey(p
						.getSecond()))
						|| ((p.getSecond().equals(node) && nodes.containsKey(p
								.getFirst())))) {
					elementExporter.exportLink(link);
				}
			} catch (ElementExportationException e) {
				// do nothing (step to next link)
			}
		}

		if (elementExporter.getDiagramTypeBasedOnElements() == DiagramType.Class) {
			Class<?> base = node.getSuperclass();
			if (base != null && nodes.containsKey(base)) {
				try {
					elementExporter.exportGeneralization(base, node);
				} catch (ElementExportationException e) {
					// do nothing
				}
			}
		}
	}

}
