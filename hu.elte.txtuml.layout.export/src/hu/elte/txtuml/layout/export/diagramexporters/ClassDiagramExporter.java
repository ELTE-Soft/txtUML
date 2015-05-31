package hu.elte.txtuml.layout.export.diagramexporters;

import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.Association;
import hu.elte.txtuml.api.AssociationEnd;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.utils.Pair;

/**
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class ClassDiagramExporter {
	
	public static boolean isNode(Class<?> cls) {
		return ModelClass.class.isAssignableFrom(cls);
	}
	
	public static boolean isLink(Class<?> cls) {
		return Association.class.isAssignableFrom(cls);
	}

	public static Pair<Class<? extends LayoutNode>, Class<? extends LayoutNode>> startAndEndOfLink(Class<?> link) {
		Class<?>[] classes = link.getDeclaredClasses();
		if (classes.length < 2) {
			return null;
		}
		Class<? extends LayoutNode> end1 = getClassTypeFromAssocEnd(classes[0]);
		Class<? extends LayoutNode> end2 = getClassTypeFromAssocEnd(classes[1]);
		if (end1 == null || end2 == null) {
			return null;
		}
		return new Pair<>(end1, end2);
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends LayoutNode> getClassTypeFromAssocEnd(Class<?> end) {
		if (!AssociationEnd.class.isAssignableFrom(end)) {
			return null;
		}
		try {
			return (Class<? extends ModelClass>) ((ParameterizedType)end.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (Exception e) {
		}
		return null;
	}
	
	// TODO when a class is shown on the diagram, all links connecting to it should be shown
}
