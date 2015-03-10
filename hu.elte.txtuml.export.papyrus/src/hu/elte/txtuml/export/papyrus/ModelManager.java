package hu.elte.txtuml.export.papyrus;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;

public class ModelManager {
	
	private IMultiDiagramEditor editor;

	public ModelManager(IMultiDiagramEditor editor) throws ServiceException{
		this.editor = editor;
	}

	
	public List<Element> getElementsOfTypes(List<String> types) throws  NotFoundException, ExecutionException, ServiceException{
		ModelSet modelSet = editor.getServicesRegistry().getService(ModelSet.class);
		UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
		Element container = (Element) umlModel.lookupRoot();
		List<Element> elements = getTypesRecursively(container, types); 
		return elements;
	}
	
	private List<Element> getTypesRecursively(Element container, List<String> types) throws ExecutionException{
 		List<Element> packages = new LinkedList<Element>();
 		for(String type : types){
	 		if(container.eClass().getName().compareTo(type) == 0){			
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
			if (act.eClass().getName().compareTo("Package") != 0){
				result.addAll(recursive(act.getOwnedElements()));
			}
		}
		return result;
	}
	
	
	
	public List<Element> getElementsOfTypeFromList(List<Element> elements, String type){
		List<Element> result = new LinkedList<Element>(); 
		for(Element element : elements){
			if (element.eClass().getName().compareTo(type) == 0){
				result.add(element);
			}
		}
		return result;
	}
	

	public List<Element> getElementsOfTypesFromList(List<Element> elements, List<String> types){
		List<Element> all = new LinkedList<Element>();
		for(String type : types){
			all.addAll(getElementsOfTypeFromList(elements, type));
		}
		return all;
	}
}
