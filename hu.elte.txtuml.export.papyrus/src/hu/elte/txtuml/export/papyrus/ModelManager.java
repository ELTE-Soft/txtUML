package hu.elte.txtuml.export.papyrus;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;;

public class ModelManager {
	
	private IMultiDiagramEditor editor;

	public ModelManager(IMultiDiagramEditor editor) throws ServiceException{
		this.editor = editor;
	}

	public Element getRoot() throws ServiceException, NotFoundException{
		ModelSet modelSet = editor.getServicesRegistry().getService(ModelSet.class);
		UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
		Element root = (Element) umlModel.lookupRoot();
		return root;
	}
	
	public List<Element> getElementsOfTypes(Element container, List<java.lang.Class<?>> types){
		List<Element> elements = getTypesRecursively(container, types); 
		return elements;
	}
	
	private List<Element> getTypesRecursively(Element container, List<java.lang.Class<?>> types){
 		List<Element> packages = new LinkedList<Element>();
 		for(java.lang.Class<?> type : types){
	 		if(isElementOfType(container, type)){			
				packages.add(container);
			}
 		}
 		
		if(!((Element) container).getOwnedElements().isEmpty()){
			List<Element> ownedElements = ((Element)container).getOwnedElements();
			for(Element ownedElement : ownedElements){
				List<Element> subpackages = getTypesRecursively(ownedElement, types);
				packages.addAll(subpackages);
			}
		}
		
		
		return packages;
	}	
	
	public List<Element> getAllElementsOfPackage(Element container){
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
		EClass eclass = element.eClass();
		Class<?> cls = eclass.getInstanceClass();
		return cls == type;
	}
}
