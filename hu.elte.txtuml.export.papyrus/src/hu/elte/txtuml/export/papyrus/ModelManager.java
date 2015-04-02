package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.utils.MultiMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

public class ModelManager {
	
	private IMultiDiagramEditor editor;
	private MultiMap<Class<?>, Element> modelMap;

	public Element getRoot() throws ServiceException, NotFoundException{
		ModelSet modelSet = editor.getServicesRegistry().getService(ModelSet.class);
		UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
		Element root = (Element) umlModel.lookupRoot();
		return root;
	}
	
	public ModelManager(IMultiDiagramEditor editor) throws ServiceException, NotFoundException{
		this.editor = editor;
		modelMap = buildUpMap();
		System.out.print(true);
	}

	private MultiMap<Class<?>, Element> buildUpMap() throws NotFoundException, ServiceException {
		MultiMap<Class<?>, Element> result = new MultiMap<Class<?>, Element>(); 
		Element root = getRoot();
		Queue<Element> queue = new LinkedList<Element>();
		queue.add(root);
		runThroughModelRecursive(queue, result);
		return result;
	}

		private void runThroughModelRecursive(Queue<Element> queue, MultiMap<Class<?>, Element> map) {
			if(!queue.isEmpty()){
				Element head = queue.poll();
				List<Element> children = head.getOwnedElements();
				if(children != null)
					queue.addAll(children);
				map.put(head.eClass().getInstanceClass(), head);
				runThroughModelRecursive(queue, map);
			}else{
				return;
			}
		}


	
	public List<Element> getElementsOfTypes(List<java.lang.Class<?>> types){
		List<Element> elements = new LinkedList<Element>();
		for(java.lang.Class<?> type : types){
			HashSet<Element> elementsoftype = modelMap.get(type);
			if(elementsoftype != null)
				elements.addAll(elementsoftype);
		}
		return elements;
	}

	public List<Element> getAllChildrenOfPackage(Element container){
		List<Element> ownedElements = new LinkedList<Element>();
		ownedElements.addAll(container.getOwnedElements());
		List<Element> allElements = recursive(ownedElements);
		return allElements;
	}
	
		private List<Element> recursive(List<Element> ownedElements){
			List<Element> result = new LinkedList<Element>();
			result.addAll(ownedElements);
			for (Element act : ownedElements) {
				if (!isElementOfType(act, Package.class)){
					result.addAll(recursive(act.getOwnedElements()));
				}
			}
			return result;
		}
	
	
	
	public List<Element> getElementsOfTypeFromList(List<Element> elements, java.lang.Class<?> type){
		List<Element> result = new LinkedList<Element>(); 
		for(Element element : elements){
			if (isElementOfType(element, type)){
				result.add(element);
			}
		}
		return result;
	}
	

	public List<Element> getElementsOfTypesFromList(List<Element> elements, List<java.lang.Class<?>> types){
		List<Element> all = new LinkedList<Element>();
		for(java.lang.Class<?> type : types){
			all.addAll(getElementsOfTypeFromList(elements, type));
		}
		return all;
	}
	
	private boolean isElementOfType(Element element, java.lang.Class<?> type){
		return element.eClass().getInstanceClass() == type;
	}
}
