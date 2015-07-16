package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.utils.MultiMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

/**
 * Controls the Eclipse UML2 Model.
 *
 * @author András Dobreff
 */
public class UMLModelManager {
	
	private UmlModel model;
	private MultiMap<Class<?>, Element> modelMap;
	
	/**
	 * The Constructor.
	 * @param model - The model to be handeled
	 */
	public UMLModelManager(UmlModel model){
		this.model = model;
		modelMap = buildUpMap();
	}

	/**
	 * Builds up a {@link MultiMap}. Hashes the Model Elements by their eClass
	 * @return The model elements in a MultiMap
	 */
	private MultiMap<java.lang.Class<?>, Element> buildUpMap(){
		MultiMap<java.lang.Class<?>, Element> result = new MultiMap<Class<?>, Element>(); 
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


	/**
	 * Returns the root of the UML model
	 * @return The root element
	 */
	public Element getRoot(){
		try{
			Element root = (Element) this.model.lookupRoot();
			return root;
		}catch(NotFoundException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the the Elements with the same type from the model. 
	 * The Elements of different types will be added sequentially. 
	 * @param types
	 * @return The Elements with same type
	 */
	public List<Element> getElementsOfTypes(List<java.lang.Class<?>> types){
		List<Element> elements = new LinkedList<Element>();
		for(java.lang.Class<?> type : types){
			HashSet<Element> elementsoftype = modelMap.get(type);
			if(elementsoftype != null)
				elements.addAll(elementsoftype);
		}
		return elements;
	}

	/**
	 * Gets the children elements of the container from model recursively.
	 * The the recursion won't be go through {@link Package}s. 
	 * @param container - The root element of the recursion
	 * @return List of children with infinite deep
	 */
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
	
	
	/**
	 * Collects the {@link Element}s of same type form a list 
	 * @param elements - List of elements
	 * @param type - The type that is searched
	 * @return Returns the Elements of same type 
	 */
	public List<Element> getElementsOfTypeFromList(List<Element> elements, java.lang.Class<?> type){
		List<Element> result = new LinkedList<Element>(); 
		for(Element element : elements){
			if (isElementOfType(element, type)){
				result.add(element);
			}
		}
		return result;
	}
	
	/**
	 * Collects the {@link Element}s of same type form a list.
	 * Elements of same type will be collected at one go
	 * @param elements - List of elements
	 * @param types - The List of types that is searched
	 * @return Returns the Elements of types
	 */
	public List<Element> getElementsOfTypesFromList(List<Element> elements, List<java.lang.Class<?>> types){
		List<Element> all = new LinkedList<Element>();
		for(java.lang.Class<?> type : types){
			all.addAll(getElementsOfTypeFromList(elements, type));
		}
		return all;
	}
	
	/**
	 * Checks if an {@link Element} is of type
	 * @param element - Element that is to be analyzed   
	 * @param type - The type
	 * @return Returns true if the Element is of type 
	 */
	private boolean isElementOfType(Element element, java.lang.Class<?> type){
		return element.eClass().getInstanceClass() == type;
	}
}
