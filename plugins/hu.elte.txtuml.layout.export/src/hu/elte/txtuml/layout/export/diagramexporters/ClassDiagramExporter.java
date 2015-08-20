package hu.elte.txtuml.layout.export.diagramexporters;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.api.layout.elements.LayoutLink;
import hu.elte.txtuml.api.layout.elements.LayoutNode;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.utils.Pair;

/**
 * 
 * @author Gábor Ferenc Kovács
 * @author Dávid János Németh
 *
 */
public class ClassDiagramExporter {
    
    private final ElementExporter elementExporter;
    private Class<?> rootElement;
    private List<Class<?>> links; // user defined links in the current model
    
    public ClassDiagramExporter(ElementExporter elementExporter, Class<?> rootElement) {
        this.elementExporter = elementExporter;
        this.rootElement = rootElement;
        this.links = new LinkedList<Class<?>>();
        
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
    public void exportAssociationsStartingFromThisNode(Class<?> node) {
	    NodeMap nodes = elementExporter.getNodes();    
	    for (Class<?> link : links) {
	        Pair<Class<? extends LayoutNode>, Class<? extends LayoutNode>> p = startAndEndOfLink(link);
	        
	        // nodes.containsKey(node) should be guaranteed here
            if ((p.getKey().equals(node) && nodes.containsKey(p.getValue())) 
                || ((p.getValue().equals(node) && nodes.containsKey(p.getKey()))))
            {
                elementExporter.exportLink((Class<? extends LayoutLink>) link);
            }
	    }
	    
	    if (elementExporter.getDiagramTypeBasedOnElements() == DiagramType.Class) {
	        Class<?> base = node.getSuperclass();
	        if (base != null && nodes.containsKey(base)) {
	            elementExporter.exportGeneralization((Class<? extends LayoutNode>) base,
	                (Class<? extends LayoutNode>) node);
	        } 
	    }
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
