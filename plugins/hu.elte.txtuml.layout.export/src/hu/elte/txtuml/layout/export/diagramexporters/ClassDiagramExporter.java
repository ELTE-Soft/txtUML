package hu.elte.txtuml.layout.export.diagramexporters;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Gabor Ferenc Kovacs
 * @author Dávid János Németh
 *
 */
public class ClassDiagramExporter extends AbstractSourceExporter implements
		SourceExporter {

	@Override
	public DiagramType getType() {
		return DiagramType.Class;
	}

	@Override
	public boolean isNode(Class<?> cls) {
		return ModelClass.class.isAssignableFrom(cls);
	}

	@Override
	public boolean isLink(Class<?> cls) {
		return Association.class.isAssignableFrom(cls);
	}

	@Override
	public Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link)
			throws ElementExportationException {
		Class<?>[] classes = link.getDeclaredClasses();
		if (classes.length < 2) {
			throw new ElementExportationException();
		}
		Class<?> end1 = getClassTypeFromAssocEnd(classes[0]);
		Class<?> end2 = getClassTypeFromAssocEnd(classes[1]);
		return new Pair<>(end1, end2);
	}

	@Override
	protected List<Class<?>> loadAllLinksFromModel(Class<?> model) {
		List<Class<?>> links = new ArrayList<>();
		for (Class<?> cls : model.getDeclaredClasses()) {
			if (isLink(cls)) {
				links.add(cls);
			}
		}
		return links;
	}

	@Override
	protected void exportImpliedLinksFromSpecifiedNode(Class<?> model,
			ElementExporter elementExporter, List<Class<?>> links, Class<?> node) {
		super.exportImpliedLinksFromSpecifiedNode(model, elementExporter,
				links, node);

		NodeMap nodes = elementExporter.getNodes();
		Class<?> base = node.getSuperclass();
		if (base != null && nodes.containsKey(base)) {
			try {
				elementExporter.exportGeneralization(base, node);
			} catch (ElementExportationException e) {
				// Exportation of implied generalization failed. Nothing to do.
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<?> getClassTypeFromAssocEnd(Class<?> end)
			throws ElementExportationException {
		if (!AssociationEnd.class.isAssignableFrom(end)) {
			return null;
		}
		try {
			return (Class<? extends ModelClass>) ((ParameterizedType) end
					.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (Exception e) {
			throw new ElementExportationException();
		}
	}
}
