package hu.elte.txtuml.layout.export.diagramexporters;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Model;
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
public class ClassDiagramExporter {

	private final ElementExporter elementExporter;
	private Class<?> rootElement;
	private List<Class<?>> links; // user defined links in the current model

	public ClassDiagramExporter(ElementExporter elementExporter,
			Class<?> rootElement) {
		this.elementExporter = elementExporter;
		this.rootElement = rootElement;
		this.links = new ArrayList<Class<?>>();

		loadLinks();
	}

	public static boolean isNode(Class<?> cls) {
		return ModelClass.class.isAssignableFrom(cls);
	}

	public static boolean isLink(Class<?> cls) {
		return Association.class.isAssignableFrom(cls);
	}

	public static boolean isModel(Class<?> cls) {
		return Model.class.isAssignableFrom(cls);
	}

	public static Pair<Class<?>, Class<?>> startAndEndOfLink(Class<?> link)
			throws ElementExportationException {
		Class<?>[] classes = link.getDeclaredClasses();
		if (classes.length < 2) {
			throw new ElementExportationException();
		}
		Class<?> end1 = getClassTypeFromAssocEnd(classes[0]);
		Class<?> end2 = getClassTypeFromAssocEnd(classes[1]);
		if (end1 == null || end2 == null) {
			throw new ElementExportationException();
		}
		return new Pair<>(end1, end2);
	}

	public void exportAssociationsStartingFromThisNode(Class<?> node) {
		NodeMap nodes = elementExporter.getNodes();
		for (Class<?> link : links) {
			try {
				Pair<Class<?>, Class<?>> p = startAndEndOfLink(link);

				// nodes.containsKey(node) should be guaranteed here
				if ((p.getFirst().equals(node) && nodes.containsKey(p.getSecond()))
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

	@SuppressWarnings("unchecked")
	private static Class<?> getClassTypeFromAssocEnd(Class<?> end) {
		if (!AssociationEnd.class.isAssignableFrom(end)) {
			return null;
		}
		try {
			return (Class<? extends ModelClass>) ((ParameterizedType) end
					.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (Exception e) {
		}
		return null;
	}

	private void loadLinks() {
		if (rootElement == null) {
			return;
		}

		for (Class<?> innerClass : rootElement.getDeclaredClasses()) {
			if (isLink(innerClass)) {
				links.add(innerClass);
			}
		}
	}

}
